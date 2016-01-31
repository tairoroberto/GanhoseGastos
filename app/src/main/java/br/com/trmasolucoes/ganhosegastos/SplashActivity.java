package br.com.trmasolucoes.ganhosegastos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.BounceAnimation;
import com.easyandroidanimations.library.ExplodeAnimation;
import com.easyandroidanimations.library.FadeInAnimation;
import com.easyandroidanimations.library.FadeOutAnimation;
import com.easyandroidanimations.library.FlipHorizontalAnimation;
import com.easyandroidanimations.library.FlipVerticalAnimation;
import com.easyandroidanimations.library.ParallelAnimator;
import com.easyandroidanimations.library.ParallelAnimatorListener;
import com.easyandroidanimations.library.PathAnimation;
import com.easyandroidanimations.library.RotationAnimation;
import com.easyandroidanimations.library.ScaleInAnimation;
import com.easyandroidanimations.library.SlideInAnimation;
import com.easyandroidanimations.library.SlideOutAnimation;

import java.util.ArrayList;


public class SplashActivity extends Activity {

    private Thread mSplashThread;
    private boolean mblnClicou = false;
    ImageView imageView;
    TextView txtSplash;
    /**************************************************************************************/
    /**             Evento chamado quando a activity é executada pela primeira vez			 */
    /**
     * ************************ *******************************************************
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.push_down_enter, R.anim.push_down_exit);
        setContentView(R.layout.activity_splash);

        //pega a imagem para girar
        imageView = (ImageView) findViewById(R.id.imgSplash);
        txtSplash = (TextView) findViewById(R.id.txtSplash);
        txtSplash.setVisibility(View.INVISIBLE);


        // Create an animation instance
/*
        Animation an = new RotateAnimation(0.0f, 100.0f, imageView.getPivotX(), imageView.getPivotY());

        // Set the animation's parameters
        an.setDuration(3500);               // horaSaida in ms
        an.setRepeatCount(0);                // -1 = infinite repeated
        an.setRepeatMode(Animation.REVERSE); // reverses each repeat
        an.setFillAfter(true);               // keep rotation after animation
*/

        //imageView.setAnimation(an);

        final AnimationListener explodeAnimListener = new AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(View.INVISIBLE);
                txtSplash.setVisibility(View.VISIBLE);
            }
        };

        final AnimationListener bounceAnimListener = new AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                new ExplodeAnimation(imageView)
                        .setListener(explodeAnimListener).animate();
            }
        };

        final ParallelAnimatorListener slideFadeInAnimListener = new ParallelAnimatorListener() {

            @Override
            public void onAnimationEnd(ParallelAnimator parallelAnimator) {
                BounceAnimation bounceAnim = new BounceAnimation(imageView);
                bounceAnim.setNumOfBounces(5);
                bounceAnim.setListener(bounceAnimListener);
                bounceAnim.animate();
            }
        };

        final ParallelAnimatorListener slideFadeOutAnimListener = new ParallelAnimatorListener() {

            @Override
            public void onAnimationEnd(ParallelAnimator parallelAnimator) {
                ParallelAnimator slideFadeInAnim = new ParallelAnimator();
                slideFadeInAnim.add(new SlideInAnimation(imageView)
                        .setDirection(Animation.DIRECTION_RIGHT));
                slideFadeInAnim.add(new FadeInAnimation(imageView));
                slideFadeInAnim.setDuration(500);
                slideFadeInAnim.setListener(slideFadeInAnimListener);
                slideFadeInAnim.animate();
            }
        };


        ParallelAnimator scaleFlipAnim = new ParallelAnimator();
        scaleFlipAnim.add(new ScaleInAnimation(imageView));
        scaleFlipAnim.setDuration(2000);
        scaleFlipAnim.setListener(slideFadeInAnimListener);
        scaleFlipAnim.animate();


        /*new ParallelAnimator()
                .add(new ScaleInAnimation(imageView))
                .setDuration(1000)
                .animate();
*/
        /**************************************************************************************/
        /**            Thead que roda enquanto espera para abrir tela principal do App			 */
        /*************************** ********************************************************/
        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {

                        wait(5000);
                        mblnClicou = true;
                    }
                } catch (InterruptedException ex) {
                }
                /**************************************************************************************/
                /**                        Se for clicado Carrega a Activity Principal					 */
                /*************************** ********************************************************/
                if (mblnClicou) {
                    finish();

                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        };

        mSplashThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        /**********************************************************************************************/
        /**     garante que quando o usuário clicar no botão "Voltar" o sistema deve finalizar a thread  */
        /*************************** ******************************************************************/
        mSplashThread.interrupt();
    }


    /**********************************************************************************************/
    /**     O método abaixo finaliza o comando wait mesmo que ele não tenha terminado sua espera	  */
    /**
     * ************************ *****************************************************************
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (mSplashThread) {
                mblnClicou = true;
                mSplashThread.notifyAll();
            }
        }
        return true;
    }

}
