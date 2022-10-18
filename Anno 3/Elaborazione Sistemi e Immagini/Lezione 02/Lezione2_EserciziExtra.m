% Corso di Elaborazione dei Segnali e Immagini
% Docente: Manuele Bicego 
% Docente Coordinatore: Marco Cristani
% Lezione 2: Esercizi extra


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Esercizio 3 
% - Caricare nel workspace l'immagine "seattle.png" e assegnarla alla variabile I
% - Costruire una nuova matrice Ih in cui ad ogni elemento di I 
%   viene sottratto il suo precedente sulle colonne (in valore assoluto).
%   In altre parole, l'elemento (i,j) della nuova matrice Ih deve essere 
%   uguale a abs( I(i,j) - I(i-1,j) ) (attenzione agli indici di inizio e fine del for)
% - Ripetere l'esercizio costruendo l'immagine Iv, ottenuta sottraendo ad ogni 
%   elemento quello adiacente sulle righe: Iv(i,j) = abs( I(i,j) - I(i,j-1) )
% - Cosa rappresentano Ih e Iv?




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Esercizio 4
% Scrivere una funzione, MYflip, che dato un vettore mu: 
% - Ne crei una copia “riflessa” (per esempio, da [1 2 3] ottengo [3,2,1]) 
% - La concateni a sinistra al vettore originale (ossia [3,2,1,1,2,3]) 




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Esercizio 5
% - Caricare l’immagine “cells.png”, che contiene una visualizzazione di 
%   cellule U2OS  (Human Bone Osteosarcoma) 
% - Provare ad evidenziare i nuclei. 
% - Suggerimento: i nuclei sono caratterizzati da valori sopra una certa 
%   soglia (provare con diverse soglie)
% - Costruire inoltre una figura con due subplot dove l'immagine originale 
%   viene affiancata all'immagine in cui sono evidenziati i nuclei.



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Esercizio 6
% - Estendere l’esercizio 2 al caso di immagini a colori, in particolare 
%   utilizzando l’immagine “peppers.png”: calcolare l’istogramma per ogni 
%   canale di colore.
% - Domanda: qual’è il canale con più valori diversi?
%   Suggerimento: contare i valori dell'istogramma diversi da zero



