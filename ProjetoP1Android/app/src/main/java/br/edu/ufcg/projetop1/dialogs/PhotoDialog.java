package br.edu.ufcg.projetop1.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.edu.ufcg.projetop1.R;
import br.edu.ufcg.projetop1.core.PhotoUser;
import br.edu.ufcg.projetop1.fragments.MapFragment;

/**
 * Created by root on 15/08/16.
 */
public class PhotoDialog extends DialogFragment {
    int mNum;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static PhotoDialog newInstance(int num, String id) {
        PhotoDialog f = new PhotoDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putString("id", id);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_photo, container, false);

        ImageView photoView = (ImageView) v.findViewById(R.id.photo);
        TextView descView = (TextView) v.findViewById(R.id.desc);
        View descLayer = v.findViewById(R.id.layer_descript);

        PhotoUser photo = MapFragment.photoUserMap.get(getArguments().getString("id"));

        photoView.setImageBitmap(photo.getPhoto());
        String desc = photo.getDescript();
        if (null != desc && !desc.equals("")) {
            descLayer.setVisibility(View.VISIBLE);
            descView.setText(desc);
        } else {
            descLayer.setVisibility(View.GONE);
        }

        return v;
    }
}