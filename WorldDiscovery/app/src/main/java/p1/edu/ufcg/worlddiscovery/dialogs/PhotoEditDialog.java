package p1.edu.ufcg.worlddiscovery.dialogs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.io.ByteArrayOutputStream;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.activities.MainActivity;
import p1.edu.ufcg.worlddiscovery.utils.PhotoUtil;

/**
 * Created by root on 04/10/16.
 */
public class PhotoEditDialog extends DialogFragment {

    private static final String IMAGE_KEY = "image";

    private Bitmap mImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        byte[] image = getArguments().getByteArray(IMAGE_KEY);
        mImage = BitmapFactory.decodeByteArray(image, 0, image.length);

        int mNum = 2;

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((mNum - 1) % 6) {
            case 1:
                style = DialogFragment.STYLE_NO_TITLE;
                break;
            case 2:
                style = DialogFragment.STYLE_NO_FRAME;
                break;
            case 3:
                style = DialogFragment.STYLE_NO_INPUT;
                break;
            case 4:
                style = DialogFragment.STYLE_NORMAL;
                break;
            case 5:
                style = DialogFragment.STYLE_NORMAL;
                break;
            case 6:
                style = DialogFragment.STYLE_NO_TITLE;
                break;
            case 7:
                style = DialogFragment.STYLE_NO_FRAME;
                break;
            case 8:
                style = DialogFragment.STYLE_NORMAL;
                break;
        }
        switch ((mNum - 1) % 6) {
            case 4:
                theme = android.R.style.Theme_Holo;
                break;
            case 5:
                theme = android.R.style.Theme_Holo_Light_Dialog;
                break;
            case 6:
                theme = android.R.style.Theme_Holo_Light;
                break;
            case 7:
                theme = android.R.style.Theme_Holo_Light_Panel;
                break;
            case 8:
                theme = android.R.style.Theme_Holo_Light;
                break;
        }
        setStyle(style, theme);
    }


    public static PhotoEditDialog onNewInstance(Bitmap imageBitmap) {
        PhotoEditDialog dialog = new PhotoEditDialog();
        Bundle args = new Bundle();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        args.putByteArray(IMAGE_KEY, stream.toByteArray());
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.photo_edit, container, false);

        ImageView imageView = (ImageView) v.findViewById(R.id.photo_to_edit);
        imageView.setImageBitmap(mImage);

        final EditText message = (EditText) v.findViewById(R.id.et_image_description);

        Button save = (Button) v.findViewById(R.id.save_photo);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoUtil.sendPhoto(mImage, ((MainActivity) getActivity()).getmLastLocation(), message.getText().toString(), getContext());
                dismiss();
            }
        });

        Button cancel = (Button) v.findViewById(R.id.cancel_photo);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }


}
