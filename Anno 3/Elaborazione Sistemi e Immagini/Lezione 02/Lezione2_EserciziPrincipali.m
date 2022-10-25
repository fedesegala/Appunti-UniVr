% Corso di Elaborazione dei Segnali e Immagini
% Docente: Manuele Bicego 
% Docente Coordinatore: Marco Cristani
% Lezione 2: esercizi principali 

%%
clear all
close all
clc


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Esercizio 1 
% - Prendete la foto di Paperino oppure fatevi una foto al volto. 
% - Copiate questa foto nella directory di lavoro, 
%   e caricatela attraverso MATLAB. 
% - Attraverso opportune indicizzazioni della matrice in cui 
%   è contenuta la foto, sostituite ai pixel che rappresentano 
%   gli occhi dei pixel neri, facendo comparire una sorta di occhiali da sole. 
% - NOTA:  Ricordo che il valore nero si ottiene con una terna RGB = [0,0,0].
% - Visualizzate l’immagine originale e quella modificata attraverso 
%   il comando surf, in due plot separati nella stessa figura
%   Per visuallizzare con surf occorre trasformare la foto in scala di grigio
%   (per una visualizzazione ottimale, usare anche il comando "shading flat")

image = imread('Paperino.jpg');
figure(1);
subplot(2,2,1)
imshow(image);
axis on;

dimensione_lenti = size(image(160:210,100:135,:));
dimensione_stanghette = size(image(185:190,135:161,:));
stanghette = zeros(dimensione_stanghette);
lenti = zeros(dimensione_lenti);

subplot(2,2,2)
surf(rgb2gray(image))
shading flat

subplot(2,2,3)

image(160:210,100:135,:) = lenti;
image(185:190,135:161,:) = stanghette;
image(160:210,161:196,:) = lenti;

imshow(image);
axis on;
grid on;

subplot(2,2,4)
surf(rgb2gray(image))
shading flat

%%
clear
clc

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Esercizio 2
% - Realizzare uno script che, data l’immagine di intensità a livelli 
%   di grigio moon.tif, conti quanti pixel (= entries i,j all’interno della matrice) 
%   assumono un particolare valore di grigio, per tutti i valori 
%   di grigio compresi tra 0 e 255. 
% - Il risultato sarà un vettore di naturali di dimensionalità (256,1) 
%   (chiamato istogramma). Provare a visualizzare questo vettore usando il comando bar.

image = imread('moon.tif');
result = zeros(1,256);

dimensione = size(image);

for i = 1:dimensione(1)
    for j = 1:dimensione(2)
        result(1,image(i,j) + 1) = result(1,image(i,j) + 1) + 1;
        %disp (image(i,j));
    end
end


% metodo più rapido
vettore_count = zeros(256,1);
for index = 0:1:255
    r = find(image(:) == index);
    vettore_count(index+1,1) = length(r);
end

bar(vettore_count);


