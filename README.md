Tisknuti Tweetu s danym hashtagem na jehlickove tiskarne pres Arduino.
======================================================================




Serverova Java aplikace stahuje z Twitteru nove tweety a posila je do Arduina pres seriovy port.
Pro univerzalnost (a pohodlnost) nekomunikuje Java app primo se seriovym portem ale pomoci programu `socat` pres TCP socket.
Proto je pred spustenim serverove app nutne nejdriv spustit:
`$ socat TCP4-LISTEN:7777,fork /dev/ttyUSB0,b9600,raw`
