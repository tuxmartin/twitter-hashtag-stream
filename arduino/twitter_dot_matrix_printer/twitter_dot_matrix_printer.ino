// https://github.com/jcw/ethercard
#include <EtherCard.h>
#define TCP_FLAGS_FIN_V 1 //as declared in net.h
#define TCP_FLAGS_ACK_V 0x10 //as declared in net.h

// printer
#define strobe 2
#define d0 3
#define d1 4
#define d2 5
#define d3 6
#define d4 7
#define d5 8
#define d6 9
#define d7 10
#define ack 12
// printer

static byte myip[]  = { 192,168,1,254 };
static byte gwip[]  = { 192,168,1,1 };
static byte mymac[] = { 0x74,0x2f,0x77,0x9f,0xe3,0x8b};
byte Ethernet::buffer[900]; // tcp ip send and receive buffer

char tweet[255];

const char reply_OK[] PROGMEM =
   "OK\r\n"
;
const char reply_BUSY[] PROGMEM =
   "BUSY\r\n"
;

void setup(){
  Serial.begin(9600);
  ether.begin(sizeof Ethernet::buffer, mymac , 10);// 53 for the mega ethernet shield and 10 for normal ethernet shield
  ether.staticSetup(myip, gwip);
 
  // printer 
  pinMode(strobe, OUTPUT);
  pinMode(d0, OUTPUT);
  pinMode(d1, OUTPUT);
  pinMode(d2, OUTPUT);
  pinMode(d3, OUTPUT);
  pinMode(d4, OUTPUT);
  pinMode(d5, OUTPUT);
  pinMode(d6, OUTPUT);
  pinMode(d7, OUTPUT);
  pinMode(ack, INPUT); 
  digitalWrite(strobe, HIGH);
  // printer
}

// TODO nenutit klienta vzdy navazovat spojeni, nechat jeno otevrene
void loop() {
    word pos = ether.packetLoop(ether.packetReceive());
    // check if valid tcp data is received
    if (pos) {
        char* data = (char *) Ethernet::buffer + pos;
        ether.httpServerReplyAck(); // send ack to the request
        
        Serial.println(*data);      
        sendToPrinter(data); // send to LPT printer        
            
        //memcpy_P(ether.tcpOffset(), reply_OK, sizeof reply_OK); // send first packet and not send the terminate flag
        //ether.httpServerReply_with_flags(sizeof reply_OK - 1,TCP_FLAGS_ACK_V);
            
        memcpy_P(ether.tcpOffset(), reply_OK, sizeof reply_OK); // send fiveth packet and send the terminate flag
        ether.httpServerReply_with_flags(sizeof reply_OK - 1,TCP_FLAGS_ACK_V|TCP_FLAGS_FIN_V);
  }
}

void sendToPrinter(char* data) {
  for (int i = 0; i < sizeof(data); i++) {
    digitalWrite(d0, bitRead(data[i],0));
    digitalWrite(d1, bitRead(data[i],1));
    digitalWrite(d2, bitRead(data[i],2));
    digitalWrite(d3, bitRead(data[i],3));
    digitalWrite(d4, bitRead(data[i],4));
    digitalWrite(d5, bitRead(data[i],5));
    digitalWrite(d6, bitRead(data[i],6));
    digitalWrite(d7, bitRead(data[i],7));
                 
    digitalWrite(strobe, 0);
    delay(2); // FIXME: cekani bude delat bordel s Ethernetem! http://arduino.cc/en/Tutorial/BlinkWithoutDelay
    digitalWrite(strobe, 1);
                 
    while (digitalRead(ack) == 0) {}
     }
}
