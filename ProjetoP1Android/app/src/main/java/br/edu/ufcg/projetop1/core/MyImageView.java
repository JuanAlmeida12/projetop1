package br.edu.ufcg.projetop1.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import br.edu.ufcg.projetop1.R;

public class MyImageView extends View {

    private Context context;

    private Bitmap bitmapBorder;
    private Bitmap bitmapMask;
    private Paint paint;
    private PorterDuffXfermode xfermode;

    private Bitmap bitmap;

    private int _width;
    private int _height;

    public MyImageView(Context context){
        this(context, null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        bitmapBorder = decodeBitmap(R.drawable.ballon_bg);
        bitmapMask = decodeBitmap(R.drawable.ballon_mask);

        _width = bitmapBorder.getWidth();
        _height = bitmapBorder.getHeight();

        paint = new Paint();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(_width, _height);
    }

    private Bitmap decodeBitmap(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public void setResourseId(int resourseId) {
        bitmap = decodeBitmap(resourseId);
        invalidate();
    }

    public void setResourseBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(bitmap == null){
            return;
        }
        canvas.drawBitmap(bitmapBorder, 0, 0, paint);
        int saveFlags = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        canvas.saveLayer(0, 0, _width, _height, null, saveFlags);
        canvas.drawBitmap(bitmapMask, 0, 0, paint);
        paint.setXfermode(xfermode);
        int left = _width/2 - bitmap.getWidth() /2;
        int top = _height/2 - bitmap.getHeight()/2;
        canvas.drawBitmap(bitmap, left, top, paint);
        paint.setXfermode(null);
        canvas.restore();
    }
}