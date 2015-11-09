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

// LPT pinout: http://pinouts.ru/ParallelPorts/ParallelPC_pinout.shtml

void setup(){
  Serial.begin(9600);

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

void loop() {
  if ( Serial.available() > 0 ) {
    char buffer = Serial.read();
    Serial.println(buffer);
    sendToPrinter(&buffer);
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
    delay(2);
    digitalWrite(strobe, 1);
                 
    while (digitalRead(ack) == 0) {}
  }
}
