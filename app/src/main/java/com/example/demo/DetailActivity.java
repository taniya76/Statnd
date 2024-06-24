package com.example.demo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Icon;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.demo.Adapters.OrdersAdapter;
import com.example.demo.Models.OrdersModel;
import com.example.demo.databinding.ActivityDetailBinding;

import java.util.ArrayList;
import java.util.Collections;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DBHelper helper=new DBHelper(this);
        ArrayList<OrdersModel> list = helper.getRecords();

        OrdersAdapter adapter=new OrdersAdapter(list,this);

        Integer price;

        if(getIntent().getIntExtra("type",0)==1){
            final int image=getIntent().getIntExtra("image",0);
            price=Integer.parseInt(getIntent().getStringExtra("price"));
            String desc=getIntent().getStringExtra("desc");
            String name=getIntent().getStringExtra("name");

            binding.detailImage.setImageResource(image);
            binding.detailFoodName.setText(name);
            binding.detailDescription.setText(desc);
            binding.priceLbl.setText(String.format("%d",price));

            binding.insertBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.R)
                @Override
                public void onClick(View view) {
                    String userName=binding.nameBox.getText().toString();
                    String userPhone=binding.phoneBox.getText().toString();

                   //if either of the field is empty then show toast
                    if(userName.length()==0 || userPhone.length()==0) {
                        Toast.makeText(DetailActivity.this,"Please Enter all the details",Toast.LENGTH_SHORT).show();
                    }
                    else if(!checkConnection(DetailActivity.this)) {
                        //if user is not connected to internet then show the
                        // dialogue to connect to internet first for placing the order
                        showConnectionDialog();
                    }
                    else{
                        //if both conditions failed then place the order
                        Boolean isInserted = helper.insertOrder(
                                userName,
                                userPhone,
                                Integer.parseInt(binding.priceLbl.getText().toString()),
                                image,
                                desc,
                                name,
                                Integer.parseInt(binding.quantity.getText().toString())
                        );
                        if (isInserted) {
                            Toast.makeText(DetailActivity.this, "Order Placed successfully", Toast.LENGTH_LONG).show();
                            addNotification(name);
                        } else {
                            Toast.makeText(DetailActivity.this, "Order is not Placed!!!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

        }else{
            int id=getIntent().getIntExtra("id",0);
            Cursor cursor=helper.getOrderById(id);

            binding.nameBox.setText(cursor.getString(1));
            binding.phoneBox.setText(cursor.getString(2));
            binding.priceLbl.setText(String.format("%d",cursor.getInt(3)));
            binding.detailImage.setImageResource(cursor.getInt(4));
            binding.quantity.setText(cursor.getString(5));
            binding.detailDescription.setText(cursor.getString(6));
            binding.detailFoodName.setText(cursor.getString(7));
            binding.insertBtn.setText("Update Order");


            price= Integer.parseInt(String.valueOf(binding.priceLbl.getText())) / Integer.parseInt(String.valueOf(binding.quantity.getText()));

            binding.insertBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //if either of the field is empty then show toast
                    if (binding.nameBox.getText().toString().length() == 0 || binding.phoneBox.getText().toString().length() == 0) {
                        Toast.makeText(DetailActivity.this, "Please Enter all the details", Toast.LENGTH_SHORT).show();
                    }
                    else if (!checkConnection(DetailActivity.this)) {
                        //if user is not connected to internet then show the
                        // dialogue to connect to internet first for placing the order
                        showConnectionDialog();
                    }
                    else {
                        //if both conditions failed then place the order
                        boolean isUpdated = helper.updateOrder(
                                binding.nameBox.getText().toString(),
                                binding.phoneBox.getText().toString(),
                                Integer.parseInt(binding.priceLbl.getText().toString()),
                                cursor.getInt(4),
                                binding.detailDescription.getText().toString(),
                                binding.detailFoodName.getText().toString(),
                                Integer.parseInt(binding.quantity.getText().toString()),
                                id
                        );

                        if (isUpdated) {
                            Toast.makeText(DetailActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(DetailActivity.this,OrderActivity.class));
//                            finish();
                        }
                        else
                            Toast.makeText(DetailActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer quantity=Integer.parseInt((String) binding.quantity.getText());
                quantity+=1;
                binding.quantity.setText(quantity.toString());

                binding.priceLbl.setText(String.format("%d",(price*quantity)));
            }
        });

        binding.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer quantity=Integer.parseInt((String) binding.quantity.getText());
                quantity-=1;

                if(quantity<=0){
                    //we are making an order
                    if(getIntent().getIntExtra("type",0)==1){
                        new AlertDialog.Builder(DetailActivity.this)
                                .setTitle("Place Order")
                                .setMessage("Are you sure you don't want to Place the Order?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();
                    }else{
                        //we are updating an order
                        String id= String.valueOf(getIntent().getIntExtra("id",0));

                        new AlertDialog.Builder(DetailActivity.this)
                                .setTitle("Delete Item")
                                .setMessage("Are you sure to delete the Order?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (helper.deleteOrder(id) > 0) {
                                            Toast.makeText(DetailActivity.this, "Order deleted", Toast.LENGTH_SHORT).show();
                                            finish();

                                        } else {
                                            Toast.makeText(DetailActivity.this, "Order not deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();
                    }
                }else {
                    binding.quantity.setText(quantity.toString());
                    binding.priceLbl.setText(String.format("%d", (price * quantity)));
                }
            }
        });
    }

    private void showConnectionDialog() {
        new AlertDialog.Builder(DetailActivity.this)
                .setTitle("Please connect to the internet to connect further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //opens the wifi page where you can connect to internet
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void addNotification(String name) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("notificationID", "notificationID", NotificationManager.IMPORTANCE_DEFAULT);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(DetailActivity.this,"notificationID")
//                .setSmallIcon(R.drawable.icon)
//                .setContentTitle("Demo App Order")
//                .setContentText("Your Order of "+ name +" Placed successfully and will reach to you soon...")
//                .setAutoCancel(true) // makes auto cancel of notification
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText("Your Order of "+ name +" Placed successfully and will reach to you very soon..."))
//                ;
//
//        Intent notificationIntent = new Intent(this,OrderActivity.class);
//
//        // Create the TaskStackBuilder and add the intent, which inflates the back stack
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addNextIntentWithParentStack(notificationIntent);
//        // Get the PendingIntent containing the entire back stack
//        PendingIntent pendingIntent =
//                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        builder.setContentIntent(pendingIntent);
//
//        // Add as notification
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(DetailActivity.this);
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(1, builder.build());


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        CharSequence Myname = "My new channel";
        String description = "Description";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        //Create the channel object//

        NotificationChannel channel = new NotificationChannel("1", Myname, importance);
        channel.setDescription(description);
        channel.setAllowBubbles(true);

        //The Activity that’ll be displayed inside our expanded bubble//

        Intent target = new Intent(DetailActivity.this, OrderActivity.class);

        //Create a PendingIntent//

        PendingIntent bubbleIntent =
                PendingIntent.getActivity(DetailActivity.this, 0, target, PendingIntent.FLAG_UPDATE_CURRENT /* flags */);

        //Create a BubbleMetadata object//


        String CATEGORY_TEXT_SHARE_TARGET =
                "com.example.category.IMG_SHARE_TARGET";

        Person chatPartner = new Person.Builder()
                .setName("Chat partner")
                .setImportant(true)
                .build();

        Notification.MessagingStyle style = new Notification.MessagingStyle(chatPartner)
                .addMessage("Your Order of "+ name +" Placed successfully and will reach to you soon...", 1, "Stationary APP")
                ;

        // Create sharing shortcut
        String shortcutId = "compose";
        ShortcutInfoCompat shortcut =
                new ShortcutInfoCompat.Builder(DetailActivity.this, shortcutId)
                        .setCategories(Collections.singleton(CATEGORY_TEXT_SHARE_TARGET))
                        .setIntent(new Intent(Intent.ACTION_DEFAULT))
                        .setLongLived(true)
                        .setShortLabel(chatPartner.getName())
                        .build();

        ShortcutManagerCompat.pushDynamicShortcut(DetailActivity.this, shortcut);


        // Create bubble metadata
        Notification.BubbleMetadata bubbleData =
                new Notification.BubbleMetadata.Builder(bubbleIntent,
                        Icon.createWithResource(DetailActivity.this,  R.drawable.icon))
                        .setDesiredHeight(600)
                        .setAutoExpandBubble(true)
                        .setSuppressNotification(true)
                        .build();

        // Create notification, referencing the sharing shortcut
        Notification.Builder builder =
                new Notification.Builder(DetailActivity.this, channel.getId())
                        .setContentIntent(bubbleIntent)
                        .setSmallIcon( R.drawable.icon)
                        .setBubbleMetadata(bubbleData)
                        .setShortcutId(shortcutId)
                        .addPerson(chatPartner)
                        .setStyle(style);


        //Submit the NotificationChannel to NotificationManager//
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(1, builder.build());
    }


    //to check if our mobile is connected to internet
    private boolean checkConnection(DetailActivity detail) {
        ConnectivityManager connectivityManager= (ConnectivityManager) detail.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null &&(actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo != null && nwInfo.isConnected();
        }
    }
}