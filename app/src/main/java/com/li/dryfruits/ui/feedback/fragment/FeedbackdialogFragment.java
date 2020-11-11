package com.li.dryfruits.ui.feedback.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.FeedbackModel;
import com.li.dryfruits.data.model.OrdersModel;
import com.li.dryfruits.util.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedbackdialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackdialogFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.cancle_card_button)
    CardView cancleCardButton;
    @BindView(R.id.feedback_txt)
    EditText feedbackTxt;
    @BindView(R.id.submit_feedback_btn)
    Button submitFeedbackBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    OrdersModel ordersModel;
    private DatabaseReference databaseFeedback;

    public FeedbackdialogFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FeedbackdialogFragment newInstance(OrdersModel ordersModel) {
        FeedbackdialogFragment fragment = new FeedbackdialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, ordersModel);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ordersModel = getArguments().getParcelable(ARG_PARAM1);
        }
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedbackdialog, container, false);
        ButterKnife.bind(this, view);
        databaseFeedback = FirebaseDatabase.getInstance().getReference().child("feedback");
        cancleCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        submitFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedbackTxt.getText().toString() != null && !feedbackTxt.getText().toString().trim().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    submitFeedbackBtn.setEnabled(false);
                    feedbackTxt.setEnabled(false);
                    String id = databaseFeedback.push().getKey();
                    String uid = AppConstants.getLoginUID(getActivity());
                    FeedbackModel feedbackModel = new FeedbackModel(id, uid, ordersModel.getOrderId(), feedbackTxt.getText().toString());
                    databaseFeedback.child(id).setValue(feedbackModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Feedback Saved", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            submitFeedbackBtn.setEnabled(true);
                            feedbackTxt.setEnabled(true);
                            Toast.makeText(getActivity(), "Error Saving feedback, Try Again Later.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    progressBar.setVisibility(View.GONE);
                    submitFeedbackBtn.setEnabled(true);
                    feedbackTxt.setEnabled(true);
                    Toast.makeText(getActivity(), "Please enter feedback", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}