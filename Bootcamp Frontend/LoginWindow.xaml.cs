using System.Windows;
using System.Windows.Input;

namespace Bootcamp_Frontend
{
    
    public partial class LoginWindow : Window
    {
        public LoginWindow()
        {
            InitializeComponent();
            
            // la ventana
            this.MouseDown += (s, e) =>
            {
                if (e.ChangedButton == MouseButton.Left)
                    this.DragMove();
            };
            
            // de username pasa a password
            UsernameTextBox.KeyDown += (s, e) =>
            {
                if (e.Key == Key.Enter)
                    PasswordBox.Focus();
            };
            
            // de password intenta login
            PasswordBox.KeyDown += (s, e) =>
            {
                if (e.Key == Key.Enter)
                    LoginButton_Click(this, new RoutedEventArgs());
            };
        }

        private void LoginButton_Click(object sender, RoutedEventArgs e)
        {
            // Limpiar mensaje de error previo
            ErrorMessageTextBlock.Visibility = Visibility.Collapsed;
            ErrorMessageTextBlock.Text = string.Empty;

            string username = UsernameTextBox.Text.Trim();
            string password = PasswordBox.Password;

            // Validacion basica
            if (string.IsNullOrWhiteSpace(username))
            {
                ShowError("Por favor, ingresa tu usuario.");
                UsernameTextBox.Focus();
                return;
            }

            if (string.IsNullOrWhiteSpace(password))
            {
                ShowError("Por favor, ingresa tu contrasena.");
                PasswordBox.Focus();
                return;
            }

            // Ejemplo temporal de validacion local
            if (username.Equals("admin", System.StringComparison.OrdinalIgnoreCase) && 
                password == "admin123")
            {
                // Login exitoso - abrir SuccessWindow
                SuccessWindow successWindow = new SuccessWindow();
                successWindow.Show();
                this.Close();
            }
            else
            {
                ShowError("Usuario o contrasena incorrectos.");
                PasswordBox.Clear();
                PasswordBox.Focus();
            }
        }

        private void CloseButton_Click(object sender, RoutedEventArgs e)
        {
            Application.Current.Shutdown();
        }

        private void ShowError(string message)
        {
            ErrorMessageTextBlock.Text = message;
            ErrorMessageTextBlock.Visibility = Visibility.Visible;
        }
    }
}
