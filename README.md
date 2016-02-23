# opennessMultiAtt
implementazione di una piattaforma per la creazione di filtri di informazioni fuzzy multiattributo

Permette di creare filtri di informazione fuzzy multiattributo; la creazione dei filtri e il loro aggiornamento sono pienamente configurabili tramite due file xml presenti in repository. Si interfaccia con un database di transazioni mysql. Può essere utilizzato un qualsiasi database,purchè contenga delle transazioni configurabili tramite xml.
Per il testing del modulo è stato utilizzato un dump di un database mysql contenente delle transazioni effettuate su un software aziendale.

Potrebbe essere utile aggiungere un interfaccia grafica per la configurazione dei filtri. Inoltre i filtri creati vengono stampati su un semplice file txt,poco maneggevole. Potrebbe essere utile implementare delle classi per la crezione di file contenenti filtri in formato csv.
