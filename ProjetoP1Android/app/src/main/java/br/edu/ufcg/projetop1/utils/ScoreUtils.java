package br.edu.ufcg.projetop1.utils;

import android.content.Context;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.projetop1.R;
import br.edu.ufcg.projetop1.core.Point;

/**
 * Created by root on 16/08/16.
 */
public class ScoreUtils {

    public static int SCORE = 0;

    private static TextView scoreview = null;

    public static List<Point> myPoints = new ArrayList<>();

    private static Context ctx = null;

    public static void scoreCount(final Context context, final FirebaseDatabase database, final TextView scoreView) {
        scoreview = scoreView;
        BadgeUtil.veryBadgesProgress(myPoints, context);
        ctx = context;
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        scoreOfPoints(ctx, database, scoreview, userid);
    }

    public static void addBadgeScore(int score) {
        SCORE += score;
        setView(scoreview, ctx);
    }

    private static void scoreOfPoints(final Context context, FirebaseDatabase database, final TextView scoreView, String userid) {
        DatabaseReference myRefplaces = database.getReference("user-place").child(userid);
        ChildEventListener visitedListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.hasChild("point")) {
                    Point point = dataSnapshot.child("point").getValue(Point.class);
                    myPoints.add(point);
                    SCORE = point.score;
                }
                setView(scoreView, context);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRefplaces.addChildEventListener(visitedListener);
    }

    private static void setView(TextView scoreView, Context context) {
        if (scoreView != null) {
            scoreView.setText(context.getString(R.string.score) + SCORE);
        }
    }
}
