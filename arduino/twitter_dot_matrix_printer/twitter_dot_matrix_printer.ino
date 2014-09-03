// https://github.com/jcw/ethercard
#include <EtherCard.h>
#define TCP_FLAGS_FIN_V 1 //as declared in net.h
#define TCP_FLAGS_ACK_V 0x10 //as declared in net.h
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
}

// TODO nenutit klienta vzdy navazovat spojeni, nechat jeno otevrene
void loop() {
    word pos = ether.packetLoop(ether.packetReceive());
    // check if valid tcp data is received
    if (pos) {
        char* data = (char *) Ethernet::buffer + pos;
        Serial.println(*data);
        
        ether.httpServerReplyAck(); // send ack to the request
            
        //memcpy_P(ether.tcpOffset(), reply_OK, sizeof reply_OK); // send first packet and not send the terminate flag
        //ether.httpServerReply_with_flags(sizeof reply_OK - 1,TCP_FLAGS_ACK_V);
            
        memcpy_P(ether.tcpOffset(), reply_OK, sizeof reply_OK); // send fiveth packet and send the terminate flag
        ether.httpServerReply_with_flags(sizeof reply_OK - 1,TCP_FLAGS_ACK_V|TCP_FLAGS_FIN_V);
  }
}
