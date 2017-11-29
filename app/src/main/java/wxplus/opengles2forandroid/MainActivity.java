package wxplus.opengles2forandroid;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // test
        click_simple_object(null);
    }


    // onclick start
    public void click_simple_color(View view) {
        startActivity(OpenGL_01_Simple_Color.class);
    }
    public void click_simple_texture(View view) {
        startActivity(OpenGL_02_Simple_Texture.class);
    }
    public void click_simple_object(View view) {
        startActivity(OpenGL_03_Simple_Object.class);
    }
}
