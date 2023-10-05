package  com.example.tuan2;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class bt1 extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckBox keepMeSignedInCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        keepMeSignedInCheckBox = findViewById(R.id.keep_me_signed_in_checkbox);

        Button signInButton = findViewById(R.id.sign_in_button);
        Button resetButton = findViewById(R.id.reset);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(bt1.this, "Vui lòng nhập username hoặc password", Toast.LENGTH_SHORT).show();
                }
                else if (username.equals("admin") && password.equals("admin1234")) {
                    Toast.makeText(bt1.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                }
                else if (password.length() < 6 || !password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*")) {
                    Toast.makeText(bt1.this, "Mật khẩu không đúng yêu cầu", Toast.LENGTH_SHORT).show();
                }
                else {
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                if (username.isEmpty()) {
                    Toast.makeText(bt1.this, "Vui lòng nhập username", Toast.LENGTH_SHORT).show();
                } else if (username.equals("admin")) {
                    Toast.makeText(bt1.this, "Reset mật khẩu thành công", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
        });
    }
}
