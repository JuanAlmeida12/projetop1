package br.edu.ufcg.projetop1.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.projetop1.R;
import br.edu.ufcg.projetop1.core.Badge;
import br.edu.ufcg.projetop1.core.Point;

/**
 * Created by root on 16/08/16.
 */
public class BadgeUtil {

    public static final String[] BADGES_TYPES = {"park", "university", "monument", "historic", "shopping", "lab"};
    public static final String FIRST = "first";
    public static final String AVANCED = "avanced";
    public static final String AVERAGE = "average";
    private static boolean running = false;
    private static Map<String, Badge> badges = new HashMap<>();
    private static List<Point> myPoints = null;

    public static void veryBadgesProgress(List<Point> points, final Context ctx) {
        if (!running) {
            myPoints = points;
            initializeBadges(ctx);
            running = true;
            new Thread(new Runnable() {
                List<Point> added = new ArrayList<Point>();

                @Override
                public void run() {
                    while (running) {
                        List<Point> points = new ArrayList<Point>(myPoints);
                        for (Point point : points) {
                            for (String type : BADGES_TYPES)
                                if (point.tags.contains(type) && !added.contains(point)) {
                                    added.add(point);
                                    final String keyFist = type + FIRST;
                                    final String keyAverage = type + AVERAGE;
                                    final String keyAvanced = type + AVANCED;

                                    final Badge badgeFirst = badges.get(keyFist);
                                    badgeFirst.increment();
                                    final Badge badgeAverage = badges.get(keyAverage);
                                    badgeAverage.increment();
                                    final Badge badgeAvanced = badges.get(keyAvanced);
                                    badgeAvanced.increment();


                                    badgeCompleted(badgeFirst, ctx);
                                    badgeCompleted(badgeAverage, ctx);
                                    badgeCompleted(badgeAvanced, ctx);

                                }
                        }
                    }
                }
            }).start();
        }
    }

    private static void badgeCompleted(Badge badge, Context ctx) {
        if (badge.badgeCompleted() && !badge.isPointsAdded()) {
            Log.e("Badge", badge.getName());
            badge.setPointsAdded(true);
            ((Activity) ctx).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ScoreUtils.addBadgeScore(100);
                }
            });
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("user-badge");
//            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            myRef.child(userid).child(key).child("completed").setValue(true);
//            myRef.child(userid).child(key).child("score").setValue(100);
        }
    }

    private static void initializeBadges(Context ctx) {
        for (String badgeType : BADGES_TYPES) {
            Badge badgeFirst = new Badge(ctx.getString(R.string.first) + " " + formartString(badgeType), 1, 0, ctx.getString(R.string.badge_first_point) + " " + formartString(badgeType));
            Badge badgeAverage = new Badge(ctx.getString(R.string.average) + " " + formartString(badgeType), 5, 0, ctx.getString(R.string.badge_average_point) + " " + formartString(badgeType));
            Badge badgeAvanced = new Badge(ctx.getString(R.string.avanced) + " " + formartString(badgeType), 10, 0, ctx.getString(R.string.badge_avanced_point) + " " + formartString(badgeType));

            badges.put(badgeType + FIRST, badgeFirst);
            badges.put(badgeType + AVERAGE, badgeAverage);
            badges.put(badgeType + AVANCED, badgeAvanced);
        }

    }

    public static List<Badge> getBadges() {
        return new ArrayList<>(badges.values());
    }

    private static String formartString(String source) {
        StringBuffer res = new StringBuffer();

        String[] strArr = source.split(" ");
        for (String str : strArr) {
            char[] stringArray = str.trim().toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            str = new String(stringArray);

            res.append(str).append(" ");
        }
        return res.toString().trim();
    }

}
