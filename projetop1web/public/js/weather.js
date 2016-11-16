function ucfirst(str) {
    var firstLetter = str.slice(0, 1);
    return firstLetter.toUpperCase() + str.substring(1);
}
var iconNames = {"Rain":"rain", "Clouds":"cloudy","Clear":"clear-day"};
var weekday = new Array(7);
weekday[0]=  "Sun";
weekday[1] = "Mon";
weekday[2] = "Tue";
weekday[3] = "Wed";
weekday[4] = "Thu";
weekday[5] = "Fri";
weekday[6] = "Sat";

var weekdayfull = new Array(7);
weekdayfull[0]=  "Sunday";
weekdayfull[1] = "Monday";
weekdayfull[2] = "Tuesday";
weekdayfull[3] = "Wednesday";
weekdayfull[4] = "Thursday";
weekdayfull[5] = "Friday";
weekdayfull[6] = "Saturday";
var idsWeathers = {};
function getWeather(lat,lng){
  var weatherUrl = 'http://api.openweathermap.org/data/2.5/forecast/daily?lat='+lat+'&lon='+ lng +'&APPID=5b865d088fbc1757b261f11d00dfde33&units=metric';
  $.getJSON(
      weatherUrl,
      function (data) {
        var weathers = [];
        for (var i = 0; i < data.list.length; i++) {
                weathers.push({
                    temp: Math.round(data.list[i].temp.day),
                    code: data.list[i].weather[0].id,
                    wind: data.list[i].speed,
                    city: data.city.name,
                    main: data.list[i].weather[0].main,
                    text: ucfirst(data.list[i].weather[0].description),
                    date: new Date(data.list[i].dt * 1000)
                });
            }
        idsWeathers["today"] = iconNames[weathers[1].main];
        var time = weathers[1].date;
        console.log(time);
        $("#today_div").append(`
          <b>`+ weekdayfull[time.getDay()] +`</b>, `+formatTime(time)+`
            <span><b>C</b></span>
          `);
        $("#city_weather").append(weathers[1].city);
        $("#city_weather").append("<br><i>"+ weathers[1].text +"</i>");
        $("#daily_weather_icon").append(`<canvas height="84" width="84" id="today"></canvas>`);

        for (var i = 1; i < weathers.length; i++) {
          var id = "weather"+i;
          idsWeathers[id] =  iconNames[weathers[i].main];
          $("#days").append(`
            <div class="col-sm-2">
              <div class="daily-weather">
                <h2 class="day">`+ weekday[weathers[i].date.getDay()] +`</h2>
                <h3 class="degrees">`+ weathers[i].temp +`</h3>
                <canvas id="`+ id +`" width="32" height="32"></canvas>
                <h5>`+ transformKmH(weathers[i].wind) +` <i>Km/h</i></h5>
              </div>
            </div>
            `)
        }


        var icons = new Skycons({
            "color": "#73879C"
          });
        var key;
        for (key in idsWeathers) {
        icons.set(key,idsWeathers[key]);
      }
        icons.play();
      }
  );
}

function formatTime(date) {
  var hours = date.getHours();
  var hours = (hours+24-2)%24;
  var min = date.getMinutes();
  var mid='AM';
  if(hours==0){ //At 00 hours we need to show 12 am
    hours=12;
  }
  else if(hours>12) {
    hours=hours%12;
    mid='PM';
  }
  hours+="";
  if(hours.length == 1) {
    hours = "0" + hours;
  }
  min+="";
  if(min.length == 1) {
    min = "0" + min;
  }
  return hours+":"+min+" "+mid;
}

function transformKmH(mps){
  return Math.floor(mps*3.6);
}
