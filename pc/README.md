Instalace na Ubuntu 14.04
-------------------------

```
# apt-get install python-tweepy python-serial
```

Spusteni na Ubuntu 14.04
------------------------

```
$ python server.py
```


Nastaveni
---------

Skoro na konci souboru najit radek

```
stream.filter(track=['#linux', '#google']) # !!!___ TADY ZMENIT HASHTAG ___!!!
```

a zmenit ho podle svych potreb. Jde zadat 1..n hashtagu/slov. Pokud je sovo bez *#*, hleda se vyskyt slova.

