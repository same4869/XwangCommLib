package commlib.xun.com.commlib.views;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import commlib.xun.com.commlib.R;

/**
 * Created by xunwang on 2017/7/3.
 */

public class CommBeatLoadingView extends FrameLayout implements View.OnClickListener {
    private Context mContext;
    private OnReloadListener onReloadListener;
    private ImageView animImageView;

    public CommBeatLoadingView(Context context) {
        super(context);
        init(context);
    }

    public CommBeatLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommBeatLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context.getApplicationContext();
        LayoutInflater.from(mContext).inflate(R.layout.comm_view_beat_loading, this);

        animImageView = (ImageView) findViewById(R.id.comm_view_beat_loading_img);
        animImageView.setBackgroundResource(R.drawable.comm_loading_dialog_w_anim);

        setClickable(false);
        setOnClickListener(this);

    }

    private void resetLoading() {
        animImageView.clearAnimation();
        animImageView.setBackgroundResource(R.drawable.comm_loading_dialog_w_anim);
        setContentText("加载中...");
    }

    /**
     * 开始加载动画
     */
    public void startLoading() {
        setVisibility(View.VISIBLE);
        findViewById(R.id.comm_view_beat_loading_img).setVisibility(View.VISIBLE);
        resetLoading();
        AnimationDrawable anim = (AnimationDrawable) animImageView.getBackground();
        if (anim != null) {
            anim.start();
        }
        setClickable(false);
    }

    /**
     * 结束加载动画并隐藏
     */
    public void endLoading() {
        animImageView.setBackgroundResource(R.drawable.comm_loading_dialog_w_anim);
        AnimationDrawable anim = (AnimationDrawable) animImageView.getBackground();
        if (anim != null) {
            anim.stop();
        }
        findViewById(R.id.comm_view_beat_loading_img).setVisibility(View.GONE);
        setVisibility(View.GONE);
        setClickable(false);
    }

    /**
     * 设置状态 无网络，加载失败，空页面等
     *
     * @param resId 显示对应的图片ID
     * @param text  显示对应的文字
     */
    public void setStaticBackground(int resId, String text) {
        setVisibility(View.VISIBLE);
        animImageView.setBackgroundResource(R.drawable.comm_loading_dialog_w_anim);
        AnimationDrawable anim = (AnimationDrawable) animImageView.getBackground();
        if (anim != null) {
            anim.stop();
        }
        try {
            findViewById(R.id.comm_view_beat_loading_img).setBackgroundResource(resId);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        findViewById(R.id.comm_view_beat_loading_img).setVisibility(View.VISIBLE);
        TextView tv = (TextView) findViewById(R.id.comm_view_beat_text);
        tv.setText(text);
    }


    /**
     * 默认使用setStaticBackground控件元素不可点击，要支持点击用这个
     *
     * @param resId
     * @param text
     */
    public void showReloadView(int resId, String text) {
        setStaticBackground(resId, text);
        setClickable(true);
    }


    public boolean isVisibility() {
        return getVisibility() == View.VISIBLE ? true : false;
    }

    /**
     * 设置状态文字
     *
     * @param text
     */
    public void setContentText(String text) {
        ((TextView) this.findViewById(R.id.comm_view_beat_text)).setText(text);
        findViewById(R.id.comm_view_beat_text).setVisibility(View.VISIBLE);
        setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏
     */
    public void hideAllLoading() {
        findViewById(R.id.comm_view_beat_loading_img).setVisibility(View.GONE);
        findViewById(R.id.comm_view_beat_text).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (onReloadListener != null) {
            onReloadListener.onReload();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onReloadListener = null;
    }

    public interface OnReloadListener {
        void onReload();
    }

    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

}
