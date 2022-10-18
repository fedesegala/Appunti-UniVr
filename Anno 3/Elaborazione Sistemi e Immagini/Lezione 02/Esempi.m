% Corso di Elaborazione dei Segnali e Immagini
% Docente: Manuele Bicego 
% Docente Coordinatore: Marco Cristani
% Lezione 2: esempi


clear all 
close all

%%%% VISUALIZZAZIONE SEGNALI

% Esempio 1: visualizzazione 
X = linspace(0,2,100)';
Y = [0.5*exp(2*X)];
figure(1) 
stem(X,Y,'Color','r','Marker','.') 
title ('Stem plot')
xlabel ('X')
ylabel ('Y')


% Esempio 2
% nuova figura
figure

%crea un vettore da 0 a 10, passo 0.01
t = [0:0.01:10]; 

%crea un'onda sinusoidale con f = 1Hz
ys = sin(2*pi*1*t); plot(t,ys); % plotta l'onda sinusoidale

% crea un coseno con f = 2 Hz
yc = cos(2*pi*2*t); 

% plotta le due onde contemporaneamente
plot(t,ys,t,yc) 
% Alternativa:
% plot(t,ys);
% hold on
% plot(t,yc);

% forza gli estremi degli assi
axis([0 10 -1.5 1.5]) 

% scrive il titolo della figura
title('Segnali') 

% scrive la label delle ascisse
xlabel('Tempo [s]') 


%%%% SUONI

% ESEMPIO 1:
% per leggere il suono
[y,Fs] = audioread('400SineWave.mp3');

% per ascoltare
sound(y(1:Fs*0.5,:),Fs)

% per visualizzarlo
t = 1:size(y(1:Fs/2,1),1);
t = t./Fs;
figure; plot(t,y(1:Fs/2,1))
xlabel('t [sec]')
ylabel('amplitude')


% ESEMPIO 2:
% Preparare il recorder per catturare un 
% suono a 44100 Hertz, 16 bit, un canale 
% (suono monaurale, ossia non stereo).
r = audiorecorder(44100,16,1)

% registro 5 secondi
recordblocking(r,5);

% ascolto quello che ho registrato
play(r)

% estraggo il segnale audio dal recorder
doubleArray = getaudiodata(r);

% visualizzo il segnale audio
figure
plot(doubleArray);
title('Audio Signal (double)');

xlabel('t [sec]')
ylabel('amplitude')
grid on



% VISUALIZZAZIONE IMMAGINI
% Esempio 1

I = imread ('cameraman.tif');
figure (1)
surf(I)
shading flat
colormap bone



% Con il seguente comando viene eseguito 
% per la visualizzazione un clipping dei 
% valori di un’immagine di intensità: 

I = imread ('cameraman.tif');
figure (1)
subplot (1,2,1), imshow (I); colorbar
subplot (1,2,2), imshow (I,[0 80]) ; colorbar


