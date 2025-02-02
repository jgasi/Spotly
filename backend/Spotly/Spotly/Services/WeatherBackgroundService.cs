
namespace Spotly.Services
{
    using System;
    using System.Net.Http;
    using System.Net.Http.Headers;
    using System.Text;
    using System.Text.Json;
    using System.Threading.Tasks;
    using Google.Apis.Auth.OAuth2;

    public class WeatherBackgroundService : BackgroundService
    {
        private readonly WeatherService _weatherService;
        private readonly NotificationSender _notificationSender;
        private readonly string _serviceAccountPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Properties", "air-projekt-ctrls-firebase-adminsdk-fbsvc-21b5a9b3ae.json");
        private readonly string _projectId = "air-projekt-ctrls";
        private readonly string _fcmEndpoint;


        public WeatherBackgroundService(WeatherService weatherService, NotificationSender notificationSender)
        {
            _weatherService = weatherService;
            _notificationSender = notificationSender;
            _fcmEndpoint = $"https://fcm.googleapis.com/v1/projects/{_projectId}/messages:send";
        }

        private async Task<string> GetAccessTokenAsync()
        {
            GoogleCredential credential = GoogleCredential.FromFile(_serviceAccountPath)
                .CreateScoped("https://www.googleapis.com/auth/firebase.messaging");

            return await credential.UnderlyingCredential.GetAccessTokenForRequestAsync();
        }

        public async Task SendPushNotificationToTopicAsync(string topic, string title, string message)
        {

            Console.WriteLine("STARTED SENDING PUSHNOTIFICATION!");
            string accessToken = await GetAccessTokenAsync();

            var payload = new
            {
                message = new
                {
                    topic = topic,
                    notification = new
                    {
                        title,
                        body = message
                    }
                }
            };

            string jsonPayload = JsonSerializer.Serialize(payload);

            using var client = new HttpClient();
            using var request = new HttpRequestMessage(HttpMethod.Post, _fcmEndpoint)
            {
                Content = new StringContent(jsonPayload, Encoding.UTF8, "application/json")
            };

            request.Headers.Authorization = new AuthenticationHeaderValue("Bearer", accessToken);

            HttpResponseMessage response = await client.SendAsync(request);
            string responseContent = await response.Content.ReadAsStringAsync();

            if (!response.IsSuccessStatusCode)
            {
                Console.WriteLine($"Failed to send notification: {response.StatusCode} - {responseContent}");
            } else
            {
                Console.WriteLine($"Notification sent successfully to topic '{topic}': {responseContent}");
            }
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            while (!stoppingToken.IsCancellationRequested)
            {
                try
                {
                    var weather = await _weatherService.GetWeatherAsync();
                    Console.WriteLine($"Temperature: {weather.Weather[0].Main}, Condition: {weather.Weather[0].Description}");
                    if (weather.Weather[0].Main == "Thunderstorm")
                    {
                        await this.SendPushNotificationToTopicAsync("weather_alerts", "Severe Weather Alert", "Expect thunderstorms in your area!");
                    }
                    if (weather.Weather[0].Main == "Snow")
                    {
                        await this.SendPushNotificationToTopicAsync("weather_alerts", "Severe Weather Alert", "Expect snow in your area!");
                    }
                } catch (Exception ex)
                {
                    Console.WriteLine($"Error fetching weather: {ex.Message}");
                }

                await Task.Delay(TimeSpan.FromHours(1), stoppingToken);
            }
        }
    }
}
