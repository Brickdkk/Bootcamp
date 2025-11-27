using System;
using System.Windows;
using System.Windows.Input;
using Bootcamp_Frontend.Services;

namespace Bootcamp_Frontend
{
    public partial class LoginWindow : Window
    {
        private readonly ApiService _apiService = new ApiService();

        public LoginWindow()
        {
            InitializeComponent();
            
            this.MouseDown += (s, e) => { if (e.ChangedButton == MouseButton.Left) this.DragMove(); };
            UsernameTextBox.KeyDown += (s, e) => { if (e.Key == Key.Enter) PasswordBox.Focus(); };
            PasswordBox.KeyDown += (s, e) => { if (e.Key == Key.Enter) LoginButton_Click(this, new RoutedEventArgs()); };
        }

        private async void LoginButton_Click(object sender, RoutedEventArgs e)
        {
            ErrorMessageTextBlock.Visibility = Visibility.Collapsed;

            try
            {
                bool exito = await _apiService.LoginAsync(UsernameTextBox.Text.Trim(), PasswordBox.Password);
                
                if (exito)
                {
                    new DashboardWindow().Show();
                    this.Close();
                }
                else
                {
                    ErrorMessageTextBlock.Text = "Usuario o contrasena incorrectos.";
                    ErrorMessageTextBlock.Visibility = Visibility.Visible;
                    PasswordBox.Clear();
                }
            }
            catch (Exception ex)
            {
                ErrorMessageTextBlock.Text = ex.Message;
                ErrorMessageTextBlock.Visibility = Visibility.Visible;
            }
        }

        private void CloseButton_Click(object sender, RoutedEventArgs e)
        {
            Application.Current.Shutdown();
        }
    }
}
