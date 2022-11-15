% Corso di Elaborazione dei Segnali e Immagini
% Docente: Manuele Bicego 
% Docente Coordinatore: Marco Cristani
% Lezione 3: Esercizi principali 



%%
%%%%%%%%%%%%%%%%%%%%%%%%%
% ESEMPI
%%%%%%%%%%%%%%%%%%%%%%%%%

%%
%%% ESEMPIO 1
% Esempio sintetico di cross-correlazione - rettangolo e triangolo
clear all
close all
clc

f1 = [1 1 1 1 1 1 1 1]; %box 
f2 = [1 2 3 4 5 6 7 8]; %triangolo
%f2 = [8 7 6 5 4 3 2 1]; %triangolo "ribaltato"

figure; 
set(gcf,'name','Cross Correlazione','IntegerHandle','off');
subplot(311); stem(f1,'Markersize',5,'MarkerFaceColor','b','Linewidth',1.1); grid on
subplot(312); stem(f2,'Markersize',5,'MarkerFaceColor','b','Linewidth',1.1); grid on
[f1xf2,lag] = xcorr(f1,f2);
subplot(313); 
plot(lag,f1xf2,'Linewidth',2); 
hold on; 
stem(lag,f1xf2,'Markersize',5,'MarkerFaceColor','r','Linewidth',1.1);
title('Cross-correlation');
xlabel('Lags');
ylabel('Corr Values');



%% 
% ESEMPIO 2 
% Esempio di cross correlazione con segnali reali
% Cross correlazione tra segnali di vibrazione su un ponte emessi da un 
% veicolo in prossimita' di due diversi sensori. La cross correlazione mi 
% aiuta a capire quanto sono diversi tra loro.
clear all
close all
clc


load noisysignals s1 s2;  % caricamento segnali. I segnali sono simili a quanto ottenuto da questi sensori
% https://www.luchsinger.it/it/sensori/vibrazioni/
[acor,lag] = xcorr(s2,s1);
[~,I] = max(abs(acor));
timeDiff = lag(I);         
figure;
subplot(411); plot(s1); title('s1');
subplot(412); plot(s2); title('s2');
subplot(413); plot(lag,acor);
title('Cross-correlation tra s1 e s2')
subplot(414); plot([zeros(1,-timeDiff) s2']); % Perche' timeDiff = -350?
hold on; plot(s1);
title('Allineamento')




%%
%%%%%%%%%%%%%%%%%%%%%%%%%
% ESERCIZI
%%%%%%%%%%%%%%%%%%%%%%%%%
%%
%%% ESERCIZIO 1
% Implementare a mano la cross correlazione 1D, partendo e completando
% lo script sottostante
clear all
close all
clc

f1 = [1 1 1 1 1 1 1 1]; %box 
f2 = [1 2 3 4 5 6 7 8]; %triangolo

M = length(f1);
N = length(f2);

% NOTA: Assumiamo per questo esercizio che f1 e f2 
% abbiano la stessa dimensione: in caso contrario si può fare 
% zero-padding, come spiegato qui di seguito:
%
% Zero padding: operazione per rendere uguali le dimensioni dei vettori (segnali) in ingresso
% if N>M 
%    f1 = cat(2,f1,zeros(1,N-M)); % concatena due vettori: cat(DIM,A,B)
%    M=N;
% elseif N<M
%    f2 = cat(2,f2,zeros(1,M-N));
% end
% 


figure; set(gcf,'name','Cross Correlazione','IntegerHandle','off');
subplot(511); stem(f1); title('f1')
subplot(512); stem(f2); title('f2')

% Primo confronto: un bin di sovrapposizione
% si aggiungono zeri a destra e a sinistra
tf1 = [zeros(1,N-1),f1,zeros(1,N-1)];
tf2 = [f2,zeros(1,2*N-2)];

lag = [-N+1:N-1];
MYf1xf2 = [];
for i=1:2*N-1
    subplot(513); stem(tf1); title('f1 allineato')
    subplot(514); stem(tf2); title('f2 allineato')
    % calcolare il valore della cross correlazione a questo punto
    MYf1xf2 = [MYf1xf2, sum(tf1 .* tf2)];
    
    % "spostare" f2 verso destra aggiungendo uno zero davanti e rimuovendolo 
    % in fondo (Alternative: si ricostruisce tf2 da zero o si utilizza 
    % circshift) 
    tf2 = circshift(tf2,1);

    % visualizzare il vettore di crosscorrelazione calcolato fino adesso
    subplot(515);
    stem(MYf1xf2);

    
    % il programma si ferma per un secondo
    pause(0.3);
end

[f1xf2, lag] = xcorr(f1,f2);
figure(2) 
stem(f1xf2);



%%
%%% ESERCIZIO 2
% Cross-correlazione su segnali audio: riconoscimento del suono attraverso 
% la cross-correlazione.
% 
% - Caricare i primi 20 secondi dei segnali audio 'funky.mp3','lost.mp3',
%   'Diana.mp3','never.mp3', 'T69.mp3'
% - Caricare il segnale audio 'Test.wav'
% - Confrontate l'esempio di test con le varie canzoni della galleria usando
%   la cross correlazione: da quale canzone proviene? 
% - Suggerimento: cercare il segnale che contiene la crosscorrelazione 
%   più grande

clear all
close all
clc

[Y1,fs1] = audioread('funky.mp3',[1,96000*20]);
[Y2,fs2] = audioread('lost.mp3',[1,96000*20]);
[Y3,fs3] = audioread('Diana.mp3',[1,96000*20]);
[Y4,fs4] = audioread('never.mp3',[1,96000*20]);
[Y5,fs5] = audioread('T69.mp3',[1,96000*20]);

test = audioread('Test.wav');


% Visualizzo un pezzo dei segnali
figure; set(gcf,'name','Dataset canzoni','IntegerHandle','off');
subplot(2,3,1); plot(Y1(1:96000*3,1));
subplot(2,3,2); plot(Y2(1:96000*3,1));
subplot(2,3,3); plot(Y3(1:96000*3,1));
subplot(2,3,4); plot(Y4(1:96000*3,1));
subplot(2,3,5); plot(Y5(1:96000*3,1));
subplot(2,3,6); plot(test(1:96000*3,1));

% Nota: segnale audio ha due canali (stereo),
% per la cross correlazione consideriamo solo il primo
% Array di celle: un metodo piu'veloce per raccogliere sequenze 
% di lunghezza diversa.
gallery{1}=Y1(:,1);
gallery{2}=Y2(:,1);
gallery{3}=Y3(:,1);
gallery{4}=Y4(:,1);
gallery{5}=Y5(:,1);

maxcc = zeros(5,1);

% DA COMPLETARE CON I PASSI MANCANTI:
% - Per ogni segnale "g" della galleria si calcola la cross correlazione
% - Si memorizza la cross correlazione massima in maxcc(g)
% - Si calcola il segnale della galleria con best match trovando il massimo
% di maxcc


test = test(:,1);
N = length(gallery{1,1});
M = length(test);

% NOTA: Assumiamo per questo esercizio che f1 e f2 
% abbiano la stessa dimensione: in caso contrario si può fare 
% zero-padding, come spiegato qui di seguito:
%
% Zero padding: operazione per rendere uguali le dimensioni dei vettori (segnali) in ingresso
if N>M 
    to_concat = zeros(N-M,1);
    test = [test; to_concat];
    M=N;
end
 

for g = 1:5
    maxcc(g) = max(xcorr(test,gallery{1,g}));
end

disp(max(maxcc));


