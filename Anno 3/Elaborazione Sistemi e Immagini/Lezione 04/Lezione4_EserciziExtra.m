% Corso di Elaborazione dei Segnali e Immagini
% Docente: Manuele Bicego 
% Docente Coordinatore: Marco Cristani
% Lezione 4: Esercizi extra 



%% Esercizio 3 - da vedere
% - Caricare l'immagine "puzzle.jpg"
% - Ritagliare un pezzo dell'immagine (suggerimento: imcrop)
% - Utilizzare la cross-correlazione per ritrovare la posizione corretta del 
%   pezzo tagliato 
%   Suggerimenti: 1. convertire l'immagine e il pezzo 
%         ritagliato in scala di grigi, la cross correlazione funziona su
%         matrici -- funzione rgb2gray
%         2. usare la cross correlazione normalizzata -- funzione
%         normxcorr2 (attenzione all'ordine dell'input)
% 
% - Visualizzare il risultato in 4 figure
%     Figura 1: immagine originale
%     Figura 2: pezzo ritagliato
%     Figura 3: matrice di cross correlazione
%     Figura 4: immagine originale a toni di grigio "attenuata"
%               con sovrapposto il pezzo estratto (a colori) nella posizione
%               corretta (Suggerimento: creare un'immagine con 3 canali 
%               (uguali all'immagine originale in scala di grigio -- 
%               per attenuare moltiplicare tutti i valori dell'immagine per 0.6)
%   


clear all
close all
clc

I = imread('puzzle.jpg');

% Ritaglia la sezione di immagine
template = imcrop(I);

I_gray = rgb2gray(I);
template_gray = rgb2gray(template);

xcorr_matrix = normxcorr2(template_gray, I_gray);

figure("WindowState","maximized");
subplot(2,2,1);
imshow(I)
subplot(2,2,2);
imshow(template);
subplot(223);
imshow(xcorr_matrix);

result_image = zeros(720,1280,3);
result_image(:,:,1) = I_gray;
result_image(:,:,2) = I_gray;
result_image(:,:,3) = I_gray;

result_image = result_image * 0.6;


target_size = [720 1280];
r = centerCropWindow2d(size(xcorr_matrix), target_size);
xcorr_matrix = imcrop(xcorr_matrix, r);

max_value = max(xcorr_matrix, [], 'all');

dim_template = size(template_gray);
coordinates = zeros(1,2);

for i = 1:720
   for j = 1:1280
        if xcorr_matrix(i,j) == max_value
            coordinates = [i j];
        end
   end
end

result_image(coordinates(1)-dim_template(1)/2:coordinates(1)+dim_template(1)/2-1, ...
    coordinates(2)-dim_template(2)/2:coordinates(2)+dim_template(2)/2-1, :) = zeros(dim_template(1), dim_template(2), 3);


result_image(coordinates(1)-dim_template(1)/2:coordinates(1)+dim_template(1)/2-1, ...
    coordinates(2)-dim_template(2)/2:coordinates(2)+dim_template(2)/2-1, :) = template;

I2 = (result_image(coordinates(1)-dim_template(1)/2:coordinates(1)+dim_template(1)/2-1, ...
    coordinates(2)-dim_template(2)/2:coordinates(2)+dim_template(2)/2-1, :));



subplot(224);
image(uint8(result_image));

%% Esercizio 4
% Cross-correlazione 2D normalizzata per trovare difetti su tessuti
% - Caricare l'immagine tex.jpg
% - Estrarre alcuni pattern in zone che non contengono difetti
% - Calcolare la cross correlazione dei pattern con l'immagine originale
% - Mediare le matrici di cross correlazione ottenute con i diversi pattern 
%   (per una maggiore robustezza)
%   le zone a bassa cross correlazione indicano i difetti
% 
% Partire dalla traccia presente in Lezione4_EserciziExtra_soluzioni.m
% 

clc
clear

% Carico immagine e la converto in scala di grigi
A = rgb2gray(imread('tex.jpg'));
[M,N] = size(A);

% Definisco una serie di pattern, tutti quadrati 14x14
R = 14;
C = 14; 
pattern1 = A(1:R,1:C); 
pattern2 = A(2:R+1,2:C+1); 
pattern3 = A(M-13:M,N-13:N);
pattern4 = A(M-14:M-1,N-14:N-1);
pattern5 = A(1:R,N-13:N);
pattern6 = A(2:R+1,N-13:N);

% Visualizzo i pattern sovrapposti all'immagine di partenza (convertita in
% scala di grigi)
figure;
imagesc(A); axis image; colormap gray; hold on;
title ('Tessitura e pattern sovrapposti')
rectangle('position',[1,1,R,C],'EdgeColor','r'); % pattern1
rectangle('position',[2,2,R,C],'EdgeColor','g'); % pattern2
rectangle('position',[M-13,N-13,14,14],'EdgeColor','b'); % pattern3
rectangle('position',[M-14,N-14,14,14],'EdgeColor','c'); % pattern4
rectangle('position',[1,N-13,14,14],'EdgeColor','m'); % pattern5
rectangle('position',[2,N-13,14,14],'EdgeColor','k'); % pattern6

% Calcolo per ogni pattern la cross-correlazione 2D (normalizzata). Attenzione all'ordine delle variabili in input! 
% L'output avra' dimensione (M+R-1,N+C-1)
% From MATLAB Help:
% C = normxcorr2(TEMPLATE,A) computes the normalized cross-correlation of
%     matrices TEMPLATE and A. The matrix A must be larger than the matrix
%     TEMPLATE for the normalization to be meaningful. The values of TEMPLATE
%     cannot all be the same. The resulting matrix C contains correlation
%     coefficients and its values may range from -1.0 to 1.0.
xcorr1 = normxcorr2(pattern1,A);
xcorr2 = normxcorr2(pattern2,A);
xcorr3 = normxcorr2(pattern3,A);
xcorr4 = normxcorr2(pattern4,A);
xcorr5 = normxcorr2(pattern5,A);
xcorr6 = normxcorr2(pattern6,A);

% Calcolo la cross-correlazione media, che avra' sempre dimensione (M+R-1,N+C-1) 
xcorr_mean = zeros(M+R-1, N+C-1);

for i=1:M+R-1
    for j=1:N+C-1
        xcorr_mean(i,j) = (xcorr1(i,j)+xcorr2(i,j)+xcorr3(i,j)+xcorr3(i,j) ...
            +xcorr4(i,j)+xcorr5(i,j)+xcorr6(i,j))/6;
    end
end


% Considero solo la parte di cross-correlazione che e' stata eseguita senza 
% gli zero-padded edges, in modo da rimuovere effetto bordo. Quindi l'output
% avra' dimensioni (M-R+1, N-C+1)
% Hint: matrice_xcorr_reduced = matrice_xcorr(r:(end-r+1),c:(end-c+1));
target_size = [M N];
r = centerCropWindow2d(size(xcorr_mean), target_size);
xcorr_reduced = imcrop(xcorr_mean, r);

% Visualizzo, in un subplot con due riquadri, il valore assoluto della
% cross-correlazione appena stimata sia come surface plot che come immagine
figure("WindowState", 'maximized');
subplot(121)
surf(xcorr_reduced);
subplot(122);
imshow(xcorr_reduced);


% Faccio il modulo della cross-correlazione appena stimata, su cui
% lavorero' da qui in avanti
xcorr_new = abs(xcorr_reduced);

% A partire dalla cross-correlazione stimata, creo una maschera
% selezionando tutti i valori inferiori a 0.2 e la visualizzo 

mask = xcorr_new;

for i = 1:M
    for j=1:N
        if mask(i,j) < 0.2
            mask(i,j) = 1;
        else
            mask(i,j) = 0;
        end
    end
end

% Creo come elemento strutturale un disco con raggio = 3, da utilizzare poi per eseguire 
% una operazione morfologica di apertura (hint: utilizzare i comandi strel e imopen)
% Il risultato deve essere una variabile chiamata mask2, che va poi
% visualizzata in una nuova figura
% Nota per IMOPEN = Perform morphological opening.
% The opening operation erodes an image and then dilates the eroded image,
% using the same structuring element for both operations.
% Morphological opening is useful for removing small objects from an image
% while preserving the shape and size of larger objects in the image.

se = strel('disk',3); 
mask2 = imopen(mask,se);


% Modifico l'immagine di partenza A in modo che abbia dimensioni uguali alla
% cross-correlazione 
% ...


% Creo una nuova immagine A1, uguale ad A. I pixel che sono pari a 1 nella variabile
% mask2 devono essere messi a 255 in A1 
A1 = A
for i = 1:M
    for j = 1:N
        if mask2(i,j) == 1
            A1(i,j) = 255;
        end
    end
end


% Visualizzo a lato immagine A e immagine con il difetto evidenziato in rosso  
Af = cat(3,A1,A,A);
figure;
imshowpair(A,Af,'montage')
title ('Immagine e Difetto finale')





%% Esempio (già svolto, solo da guardare) 
%  Video stabilizzazione

clear all
close all
load frames; % video preso da: https://www.youtube.com/watch?v=wHsrBJ4ynk4
[nr,nc,ns,nt] = size(frames);
Res = 1.2; 

% Eseguo per tutti i frame una operazione di resize utilizzando il valore
% di scala Res sopra definito e salvo in una nuova variabile temporanea, sempre 4D, che
% chiameremo sub
for i = 1:nt
    sub(:,:,:,i) = imresize(frames(:,:,:,i),Res);
end

frames = sub;
clear sub;
[nR,nC,nS,nT] = size(frames);

% Eseguo operazione di crop nel frame n.10 per trovare una zona di ancoraggio e 
% salvo il risultato in una variabile chiamata template. 
% Converto template in scala di grigi 
figure; template = imcrop(frames(:,:,:,10));
template = rgb2gray(template);
[A,B] = size(template);

figure;
for i=1:nT
    % Prendo il frame e lo salvo in una variabile temporanea comp, che
    % converto poi in scala di grigi (compg)
    comp  = frames(:,:,:,i);
    compg = rgb2gray(comp);
    
    % Eseguo la cross-correlazione normalizzata tra questa e il template.
    % Individuo il punto di massima cross-correlazione e relativo offset
    % (corr_offset)
    cc = normxcorr2(template,compg);
    [max_cc, imax] = max((cc(:)));
    [ypeak, xpeak] = ind2sub(size(cc),imax(1));
    corr_offset = [(ypeak-A+1) (xpeak-B+1)];
   
    % Capire cosa viene eseguito in queste righe
    offset = [-(corr_offset(2)-round(nC/2)-1) -(corr_offset(1)-round(nR/2)-1)];
    new(:,:,:,i) = imtranslate(frames(:,:,:,i),offset,'FillValues',0);
    ccs(:,:,i) = cc;
    
    % Ad ogni ciclo, quindi per ogni frame, definire quattro subplot e visualizzare:
    % 1. template, 2. compg, 3. ccs con evidenziata in sovrapposizione la posizione del picco,
    % 4. new
    subplot(221); imagesc(template); axis image; title('Template scelto');
    subplot(222); imagesc(compg); axis image;  title(strcat('Immagine originale: ', num2str(i)));
    subplot(223); imagesc(ccs(:,:,i)); colorbar; title('Mappa di cross-correlazione 2D');
    hold on;      scatter(xpeak, ypeak,'rX');
    subplot(224); imshow(new(:,:,:,i)); title('Immagine stabilizzata');
    disp (['Frame numero ', num2str(i)])
    pause(0.1)
end
%
% Codice che mi permette di visualizzare affiancati il video originale e
% quello stabilizzato, e infine di salvare in un .mp4 il risultato
vidObj = VideoWriter('Stabilized_video.mp4');
open(vidObj);
figure;
for i=1:nT
    subplot(121); imshow(frames(:,:,:,i));
    subplot(122); imshow(new(:,:,:,i));
    currFrame = getframe(gcf);
    writeVideo(vidObj,currFrame);

end 
close(vidObj);
   
% Esempi tosti di video stabilization si trovano in https://www.youtube.com/watch?v=I6E6InIQ76Q
%  Li possiamo fare anche noi una volta studiate le rotazioni delle
%  immagini. Quelli piu' complicati si fanno dopo aver visto tre corsi
%  visual di riconoscimento e visione computazionale. Li infatti si fa
%  tracking con trasformazioni affini
