% Corso di Elaborazione dei Segnali e Immagini
% Docente: Manuele Bicego 
% Docente Coordinatore: Marco Cristani
% Lezione 1: esercizi principali 

clear all
close all
clc

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Esercizio 1 
% Definire i seguenti tre vettori: 
% - A vettore riga che contiene i numeri pari da 2 fino a 20, 
% - B vettore riga con tutti i numeri da -22 a -13, 
% - C vettore riga con 10 valori uguali a 0. 
% 
% A partire da questi, effettuare le seguenti operazioni
% - Creare una matrice MatX dove le righe sono costituite da A, B e C (in questo ordine) 
% - Verificare e salvare le dimensioni di MatX e il numero di elementi
% - Estrarre la sotto-matrice che contiene le prime due righe e le prime cinque colonne
% - Sostituire la seconda colonna di MatX con il valore 31
% - Creare una matrice MatY di numeri reali distribuiti in modo random (randn), con 4 righe e 10 colonne
% - Creare una matrice MatZ data dalla concatenazione di MatX e MatY
% - Verificare le dimensioni di MatZ ed estrarre la diagonale.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%
clear all
close all 
clc

A = 2:2:20;
B = -22:-13;
C = zeros(1,10);

MatX = [A;B;C];

dimensioni = size(MatX);
elementi = numel(MatX);

subMatX = MatX(1:2,1:5);

MatX(:,2) = 31;

MatY = randn(4,10);

MaxZ = [MatX;MatY];

dimensioniZ = size(MaxZ);
diagonaleZ = diag(MaxZ);

%%
clear all 
close all
clc

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Esercizio 2
% - Generare un numero casuale con il comando randn 
%   (distribuzione normale standard)
% - Assegnare alla variabile y il valore 1 se tale numero 
%   e' compreso tra -1 e 1 (media +- deviazione standard), 0 altrimenti.
% - Se ripeto il procedimento 10000 volte, quante volte il numero casuale 
%  cade nell'intervallo [-1 1]?
%
% EXTRA: Provare a risolvere l'esercizio anche senza usare cicli 
% (suggerimento: consultate l'help della funzione randn)
    
rand = randn(1,1);

if (abs(rand) <= 1)
    y = 1;
else
    y = 0;
end

rand_vector = randn(1,10000);

logical_vector = abs(rand_vector) <= 1;

conteggio = sum(logical_vector);

%%
clear all
close all 
clc


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Esercizio 3
% - Creare una function chiamata Mymean che dato un vettore o 
%   una matrice in ingresso restituisca il valore medio. 
% - In particolare, nel caso di vettori la function restituisce un singolo valore medio, 
%   mentre per le matrici un vettore riga contenente il valor medio di ogni colonna. 
% - Controllare che la funzione dia il risultato atteso (confronto con il risultato della funzione mean di matlab) 
%   con in ingresso un vettore riga, un vettore colonna e una matrice


vec = 1:2:30;
mat = [1:2:30;ones(1,15);16:30];
vec_media = Mymean(vec);
media_Matlab = mean(vec');
confronto = [vec_media; media_Matlab]



