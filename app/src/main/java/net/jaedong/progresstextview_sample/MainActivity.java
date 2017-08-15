package net.jaedong.progresstextview_sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.jaedong.progresstextview.ProgressListener;
import net.jaedong.progresstextview.ProgressTextView;

public class MainActivity extends AppCompatActivity {

    private TextView startButton;
    private ProgressTextView progressTextView;
    private EditText secEditText;
    private ProgressTextView.SpannableTask target = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressTextView = (ProgressTextView) findViewById(R.id.text);
        progressTextView.addProgressListener(new ProgressListener() {
            @Override
            public void complete() {
                Toast.makeText(MainActivity.this, "Complete", Toast.LENGTH_SHORT).show();
                cancelProgress();
            }
        });

        secEditText = (EditText) findViewById(R.id.sec);
        startButton = (TextView) findViewById(R.id.emptyButton);
        startButton.setTag(0);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type = (int) view.getTag();
                if (type == 0) {
                    startProgress();
                } else {
                    cancelProgress();
                }
            }
        });
    }

    private void cancelProgress() {
        // cancel all
        target.cancel();

        startButton.setText("Start");
        startButton.setTag(0);
    }

    private void startProgress() {
        target = progressTextView.getSpannableTask();
        // target.runTask(1.3f);
        // target.runTask(5000);
        target.runTask(Integer.parseInt(secEditText.getText().toString()) * 1000);

        startButton.setText("Cancel");
        startButton.setTag(1);
    }
}
