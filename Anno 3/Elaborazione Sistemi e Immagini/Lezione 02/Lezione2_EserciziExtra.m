% Corso di Elaborazione dei Segnali e Immagini
% Docente: Manuele Bicego 
% Docente Coordinatore: Marco Cristani
% Lezione 2: Esercizi extra

%%

clear all
clc

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

I = imread('seattle.png');
imshow(I);
dim= size(I); 

Ih = I;


for i = 1:dim(1)
    for j = 1:dim(2)
        if i == 1
            Ih(1,j) = abs(I(1,j) - I(dim(1),j));
        else
            Ih(i,j) = abs(I(i,j) - I(i-1, j));
        end
    end
end



%%
clear all 
clc

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Esercizio 4
% Scrivere una funzione, MYflip, che dato un vettore mu: 
% - Ne crei una copia “riflessa” (per esempio, da [1 2 3] ottengo [3,2,1]) 
% - La concateni a sinistra al vettore originale (ossia [3,2,1,1,2,3]) 

A = [1 2 3];
x = MyFlip(A)

%%
clear all
clc

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Esercizio 5
% - Caricare l’immagine “cells.png”, che contiene una visualizzazione di 
%   cellule U2OS  (Human Bone Osteosarcoma) 
% - Provare ad evidenziare i nuclei. 
% - Suggerimento: i nuclei sono caratterizzati da valori sopra una certa 
%   soglia (provare con diverse soglie)
% - Costruire inoltre una figura con due subplot dove l'immagine originale 
%   viene affiancata all'immagine in cui sono evidenziati i nuclei.


I = imread('cells.png');
I1 = I;

figure(1)
subplot(1,2,1);
imshow(I);

subplot(1,2,2);
dim = size(I1);
for i=1:dim(1)
    for j=1:dim(2)
        if I1(i,j) > 53
            I1(i,j) = 255;
        end
    end
end

imshow(I1);

%%
clear all 
clc

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Esercizio 6
% - Estendere l’esercizio 2 al caso di immagini a colori, in particolare 
%   utilizzando l’immagine “peppers.png”: calcolare l’istogramma per ogni 
%   canale di colore.
% - Domanda: qual’è il canale con più valori diversi?
%   Suggerimento: contare i valori dell'istogramma diversi da zero

% da vedere risposta alla domanda

I = imread('peppers.png');
figure(1)
imshow(I);

red = I(:,:,1);
green = I(:,:,2);
blue = I(:,:,3);
dim = size(I);

figure (2)
subplot(3,1,1);
red_y = zeros(1,256);
for i = 1:dim(1)
    for j=1:dim(2)
        red_y(1, red(i,j) + 1) = red_y(1, red(i,j) + 1) + 1;
    end
end

bar(red_y,'red');

subplot(3,1,2);
green_y = zeros(1,256);
for i = 1:dim(1)
    for j=1:dim(2)
        green_y(1, green(i,j) + 1) = green_y(1, green(i,j) + 1) + 1;
    end
end

bar(green_y,'green');

subplot(3,1,3);
blue_y = zeros(1,256);
for i = 1:dim(1)
    for j=1:dim(2)
        blue_y(1, blue(i,j) + 1) = blue_y(1, blue(i,j) + 1) + 1;
    end
end

bar(blue_y);


