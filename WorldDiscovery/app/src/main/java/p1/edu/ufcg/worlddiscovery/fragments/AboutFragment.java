package p1.edu.ufcg.worlddiscovery.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.HashMap;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.core.User;
import p1.edu.ufcg.worlddiscovery.utils.UserUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {

    private TextView message;
    private TextView address;
    private TextView gender;
    private TextView job;

    private EditText etJob;
    private EditText etAddress;
    private RadioGroup rgGender;
    private EditText etMessage;
    private RadioButton male;
    private RadioButton female;

    private FloatingActionMenu defaultMenu;
    private FloatingActionMenu editMenu;

    private FloatingActionButton follow;
    private FloatingActionButton edit;
    private FloatingActionButton save;
    private FloatingActionButton cancel;


    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param about Parameter 1.
     * @return A new instance of fragment AboutFragment.
     */
    public static AboutFragment newInstance(Bundle about) {
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(about);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_about, container, false);

        Activity activity = AboutFragment.this.getActivity();

        defaultMenu = (FloatingActionMenu) activity.findViewById(R.id.fm_defaut);
        editMenu = (FloatingActionMenu) activity.findViewById(R.id.fm_edit);

        edit = (FloatingActionButton) activity.findViewById(R.id.fb_edit);
        save = (FloatingActionButton) activity.findViewById(R.id.fb_save);
        cancel = (FloatingActionButton) activity.findViewById(R.id.fb_cancel);

        Bundle about = getArguments();

        if (about.getBoolean(UserDetailDetailFragment.IS_CURRENT_USER_KEY)) {
            edit.setVisibility(View.VISIBLE);
        } else {
            edit.setVisibility(View.GONE);
        }

        job = (TextView) mView.findViewById(R.id.userJob);

        gender = (TextView) mView.findViewById(R.id.userGender);

        address = (TextView) mView.findViewById(R.id.userAddress);

        message = (TextView) mView.findViewById(R.id.userMessage);

        etAddress = (EditText) mView.findViewById(R.id.et_address);
        etJob = (EditText) mView.findViewById(R.id.et_job);
        etMessage = (EditText) mView.findViewById(R.id.et_message);
        rgGender = (RadioGroup) mView.findViewById(R.id.rg_gender);
        male = (RadioButton) mView.findViewById(R.id.rb_male);
        female = (RadioButton) mView.findViewById(R.id.rb_female);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMenu.setVisibility(View.VISIBLE);
                defaultMenu.setVisibility(View.GONE);
                etAddress.setVisibility(View.VISIBLE);
                etAddress.setText(address.getText());
                address.setVisibility(View.GONE);
                etJob.setVisibility(View.VISIBLE);
                etJob.setText(job.getText());
                job.setVisibility(View.GONE);
                etMessage.setVisibility(View.VISIBLE);
                etMessage.setText(message.getText());
                message.setVisibility(View.GONE);
                rgGender.setVisibility(View.VISIBLE);
                gender.setVisibility(View.GONE);
                if (gender.getText().equals(R.string.male)) {
                    male.setChecked(true);
                } else if (gender.getText().equals(getString(R.string.female))) {
                    female.setChecked(true);
                } else {
                    male.setChecked(true);
                    female.setChecked(false);
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMenu.setVisibility(View.GONE);
                defaultMenu.setVisibility(View.VISIBLE);
                etAddress.setVisibility(View.GONE);
                address.setVisibility(View.VISIBLE);
                etJob.setVisibility(View.GONE);
                job.setVisibility(View.VISIBLE);
                etMessage.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                rgGender.setVisibility(View.GONE);
                gender.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> update = new HashMap();

                editMenu.setVisibility(View.GONE);
                defaultMenu.setVisibility(View.VISIBLE);

                String newAddress = etAddress.getText().toString();
                etAddress.setVisibility(View.GONE);
                address.setVisibility(View.VISIBLE);
                if (!address.getText().toString().equals(newAddress)) {
                    address.setText(newAddress);
                    update.put("city", newAddress);
                }

                String newJob = etJob.getText().toString();
                etJob.setVisibility(View.GONE);
                job.setVisibility(View.VISIBLE);
                if (!job.getText().toString().equals(newJob)) {
                    job.setText(newJob);
                    update.put("job", newJob);
                }

                String newMessage = etMessage.getText().toString();
                etMessage.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                if (!message.getText().toString().equals(newMessage)) {
                    message.setText(newMessage);
                    update.put("message", newMessage);
                }

                String newGender = ((RadioButton) mView.findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();
                rgGender.setVisibility(View.GONE);
                gender.setVisibility(View.VISIBLE);
                if (!gender.getText().toString().equals(newGender)) {
                    gender.setText(newGender);
                    update.put("gender", newGender);
                }
                UserUtils.updateUser(update);
            }
        });

        job.setText(about.getString(UserDetailDetailFragment.JOB_KEY));
        gender.setText(about.getString(UserDetailDetailFragment.GENDER_KEY));
        address.setText(about.getString(UserDetailDetailFragment.ADDRESS_KEY));
        message.setText(about.getString(UserDetailDetailFragment.MESSAGE_KEY));


        return mView;
    }

}
