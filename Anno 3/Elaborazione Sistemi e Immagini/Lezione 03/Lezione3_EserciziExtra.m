% Corso di Elaborazione dei Segnali e Immagini
% Docente: Manuele Bicego 
% Docente Coordinatore: Marco Cristani
% Lezione 3: Esercizi extra 



%%
%%%%%%%%%%%%%%%%
% ESERCIZIO 3 
% Provare la cross correlazione con differenti segnali definiti a mano.
% Analizzare l'help di xcorr per adottare anche la versione normalizzata
% 
% Provare a calcolare la cross correlazione (normalizzata e non) 
% di (f1,f2) e di (f1,f3)
% Domanda: quando ha senso utilizzare la versione normalizzata?
% 
%

clear all
close all
clc


f1 = [1 1 1 1 1 1 1 1]; 
f2 = [1 2 3 4 5 6 7 8]; 
f3 = [0.1 0.1 0.1 0.1 0.1 0.1 0.1 0.1];


figure
subplot(311)
stem(f1)
title('f1')
subplot(312)
stem(f2)
title('f2')
subplot(313)
stem(f3)
title('f3')


% Calcolo e visualizzo crosscorrelazione f1,f2
figure (2); 
subplot(221);
stem(xcorr(f1,f2))
title('xcorr(f1,f2)')
subplot(222);
stem(xcorr(f1,f2,"normalized"))
title('normalized xcorr f1,f2')



% Calcolo crosscorrelazione f1,f3
subplot(223)
stem(xcorr(f1,f3))
title('xcorr f1,f3')
subplot(224);
stem(xcorr(f1,f3,"normalized"))
title('normalized xcorr f1,f3')




%%
%%%%%%%%%%%%%%%%
% ESERCIZIO 4 
% Cross-correlazione 1D tra segnali di risonanza magnetica funzionale.
% - Caricare il file "Ab_pASL_Yeo_Average.txt": contiene segnali medi 
%   di fMRI (functional MRI) di un soggetto, misurati per 200 istanti 
%   in 100 diverse regioni del cervello
% - utilizzare questa informazione per estrarre delle matrici di
%   connettivita', che spiegano come le aree cerebrali comunicano tra di
%   loro. In particolare:
%   - sottrarre da ogni segnale la sua media
%   - calcolare per ogni coppia di segnali (i,j) la cross-correlazione normalizzata.
%   - salvare il massimo di tale cross-correlazione nella posizione (i,j)
%   della matrice "matrice_xcorr_max"
% 
% - Questa matrice rappresenta una possibile stima di una matrice di 
%   connettività, che indica la similarità tra le diverse regioni del
%   cervello. Visualizzare la matrice e rispondere alla seguente domanda: 
%   quali sono le due regioni più simili? 
%


clear all
close all
clc

load Ab_pASL_Yeo_Average.txt
[nr,nc] = size(Ab_pASL_Yeo_Average);

% Visualizzo due segnali nello stesso plot
figure(1)
plot(Ab_pASL_Yeo_Average(:,3),'r','LineWidth',2)
hold on
plot(Ab_pASL_Yeo_Average(:,4),'b','LineWidth',2)
xlim([0 200])
grid on
title ('Segnali 3 e 4')


% Visualizzo nello stesso plot due segnali a cui ho sottratto la media
figure(2)
plot(Ab_pASL_Yeo_Average(:,3)-mean(Ab_pASL_Yeo_Average(:,3)),'r','LineWidth',2)
hold on
plot(Ab_pASL_Yeo_Average(:,4)-mean(Ab_pASL_Yeo_Average(:,4)),'b','LineWidth',2)
xlim([0 nc])
grid on
title ('Segnali 3 e 4 - Demeaned')
% Adesso sono molto più comparabili


% - utilizzare questa informazione per estrarre delle matrici di
%   connettivita', che spiegano come le aree cerebrali comunicano tra di
%   loro. In particolare:
%   - sottrarre da ogni segnale la sua media
%   - calcolare per ogni coppia di segnali (i,j) la cross-correlazione normalizzata.
%   - salvare il massimo di tale cross-correlazione nella posizione (i,j)
%   della matrice "matrice_xcorr_max"
% 
% - Questa matrice rappresenta una possibile stima di una matrice di 
%   connettività, che indica la similarità tra le diverse regioni del
%   cervello. Visualizzare la matrice e rispondere alla seguente domanda: 
%   quali sono le due regioni più simili? 

% sottraggo la sua media ad ognuno dei segnali 

matrice_xcorr_max = zeros(nc,nc);

for i = 1:nc
    Ab_pASL_Yeo_Average(:,i) = Ab_pASL_Yeo_Average(:,i) - mean(Ab_pASL_Yeo_Average(:,i));
end

for i=1:nc
    for j=1:nc
        if i ~= j
            matrice_xcorr_max(j,i) = max(xcorr(Ab_pASL_Yeo_Average(:,i), Ab_pASL_Yeo_Average(:,j), 'normalized'));
        end
    end
end

% all serve per trovare il massimo tra tutti gli elementi della matrice
[M, I] = max(matrice_xcorr_max, [], "all" );



% prendo gli indici di riga e colonna dell'elemento con cross correlazione
% maggiore
[row, col] = ind2sub(size(matrice_xcorr_max), I);

disp("Le regioni più compatibili sono " + col + " e " + row);



