using Microsoft.AspNetCore.Mvc;
using Spotly.DTOs;
using Spotly.Services;

namespace Spotly.Controllers
{
    [ApiController]
    [Route("api/weather")]
    public class WeatherController : ControllerBase
    {
        private readonly WeatherService _weatherService;

        public WeatherController(WeatherService weatherService)
        {
            _weatherService = weatherService;
        }

        [HttpGet]
        public async Task<ActionResult<WeatherResponse>> GetWeather()
        {
            var weatherData = await _weatherService.GetWeatherAsync();

            if (weatherData == null)
            {
                return NotFound("Weather data could not be retrieved.");
            }

            return Ok(weatherData);
        }
    }
}
