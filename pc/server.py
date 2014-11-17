
#Import the necessary methods from tweepy library
from tweepy.streaming import StreamListener
from tweepy import OAuthHandler
from tweepy import Stream

import json
from pprint import pprint

from datetime import datetime
import unicodedata

import serial

#Variables that contains the user credentials to access Twitter API 
access_token = "2786658612-jdwakIPHksWwS4GUEGIfYxrBuC5OdKzf7HptNh5"
access_token_secret = "ENlq9L6mj42Z4JWiYOuasXoEnpHPmhFMAeddICQrP0eaL"
consumer_key = "rDaFBZQFxUSTpwClJtJIP0L03"
consumer_secret = "nesw5BmxVHum47KMOl5SuWNOdX5pju8WbWFwW5zvtx7WC2atwr"

# configure the serial connections (the parameters differs on the device you are connecting to)
ser = serial.Serial(
	#port='/dev/ttyUSB0',
	port='/dev/ttyS0',
	baudrate=9600,
	#writeTimeout = 2,
	parity=serial.PARITY_NONE,
	stopbits=serial.STOPBITS_ONE,
	bytesize=serial.EIGHTBITS
)

try: 
    ser.open()
except Exception, e:
    print "error open serial port: " + str(e)
    exit()

#This is a basic listener that just prints received tweets to stdout.
class StdOutListener(StreamListener):

	def on_data(self, data):
		#print data

		cas = datetime.now().strftime('%Y-%m-%d %H:%M:%S')

		tweet = json.loads(data)
		print tweet
		print " "
		#print tweet["text"]

		vystup = "--------------------------------------" "\n" 
		vystup += "Datum a cas: " + cas + "\n" 
		#vystup += "ID= " + str(tweet["id"]) + "\n" 
		vystup += "USER= " + tweet["user"]["screen_name"] + "\n" 
		vystup += "TEXT= " + tweet["text"] + "\n" 
		vystup += "--------------------------------------"

		asciiVystup = unicodedata.normalize('NFKD', vystup).encode('ascii','ignore') # protoze jehlickova tiskarna neumi UTF-8 :-)

		print asciiVystup

		if ser.isOpen():
			try:
				#ser.flushInput() #flush input buffer, discarding all its contents
				ser.flushOutput()#flush output buffer, aborting current output 
				ser.write(asciiVystup)
				ser.write("\n") # vyzkouset, jestli to je potreba!
			except Exception, e1:
				print "error communicating...: " + str(e1)
		else:
			print "cannot open serial port "

		return True

	def on_error(self, status):
		print status


if __name__ == '__main__':
	#This handles Twitter authetification and the connection to Twitter Streaming API
	l = StdOutListener()
	auth = OAuthHandler(consumer_key, consumer_secret)
	auth.set_access_token(access_token, access_token_secret)
	stream = Stream(auth, l)

	#This line filter Twitter Streams to capture data by the keywords: 'python', 'javascript', 'ruby'
	#stream.filter(track=['python', 'javascript', 'ruby'])
	stream.filter(track=['linux'])




# pouzite zdroje:
#		http://adilmoujahid.com/posts/2014/07/twitter-analytics/
#		http://stackoverflow.com/a/15589849/1974494


