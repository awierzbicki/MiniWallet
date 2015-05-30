package com.github.miniwallet.maps;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.TextView;

import com.github.miniwallet.R;
import com.github.miniwallet.shopping.Purchase;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class PurchaseRenderer extends DefaultClusterRenderer<Purchase> {
    private final IconGenerator iconGenerator;
    private final TextView priceText;

    public PurchaseRenderer(Activity activity, GoogleMap map, ClusterManager<Purchase> clusterManager) {
        super(activity, map, clusterManager);

        iconGenerator = new IconGenerator(activity);
        View multiProfile = activity.getLayoutInflater().inflate(R.layout.multi_profile, null);
        iconGenerator.setContentView(multiProfile);
        priceText = (TextView) multiProfile.findViewById(R.id.price);
    }

    @Override
    protected void onBeforeClusterItemRendered(Purchase purchase, MarkerOptions markerOptions) {
        createItemView(markerOptions, String.valueOf(purchase.getPrice()) + " PLN", purchase.getProduct().getName());
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Purchase> cluster, MarkerOptions markerOptions) {
        Double prices = 0D;

        for (Purchase p : cluster.getItems()) {
            prices += p.getPrice();
        }

        createItemView(markerOptions, String.valueOf(prices) + " PLN", String.valueOf(cluster.getSize()));
    }

    private void createItemView(MarkerOptions markerOptions, String priceText, String iconText) {
        this.priceText.setText(priceText);
        Bitmap icon = iconGenerator.makeIcon(iconText);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return cluster.getSize() > 1;
    }
}
