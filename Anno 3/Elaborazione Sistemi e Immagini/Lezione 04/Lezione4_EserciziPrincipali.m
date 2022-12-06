% Corso di Elaborazione dei Segnali e Immagini
% Docente: Manuele Bicego 
% Docente Coordinatore: Marco Cristani
% Lezione 4: Esercizi principali 



%% Esempio 1 - Cross-correlazione 2D
% Controllate rispetto a quanto visto in teoria se tornano i conti: 
 
X1 =[1     2     5     2     5;
     3     4     5     4     2;
     5     2     3     2     2;
     2     3     2     4     2;
     3     4     2     2     3];
     
     
X2 =[3     3     2;
     2     3     4;
     4     5     4];
 
Corr = xcorr2(X1,X2);
size(Corr)
 

%% Esercizio 1
% Matching 2D
% Usare la cross correlazione 2D per trovare la posizione del template 
% nell'immagine
% In particolare si richiede di calcolare di quanto (righe-colonne) 
% il template è stato traslato rispetto all’angolo in alto a sinistra 
% dell’immagine
%
% Suggerimento: calcolare la cross correlazione (xcorr2) tra l'immagine 
% e il template (kernel), estrarre le coordinate del massimo e 
% recuperare la posizione del kernel
% 
% Controllare anche l'help della funzione xcorr2

clear all
close all

% Template: una croce
template = 0.2*ones(55);
template(29:31,15:45) = 0.6;
template(15:45,29:31) = 0.6;


% Immagine: si posiziona il template con un offset 
immagine = 0.2*ones(111);
offset = [10 40];
immagine(offset(1):offset(1)+size(template,1)-1,offset(2):offset(2)+size(template,2)-1) = template;

figure
imshow(template,[])
title('Template')

figure
imshow(immagine,[])
title('Immagine')

[row_template, col_template] = size(template);

result = xcorr2(immagine,template);
[M, I] = max(result, [], "all");
[row, col] = ind2sub(size(result), I);

offset_x = row - row_template + 1;
offset_y = col - col_template + 1;


disp("offset x: " +offset_x + " offset_y: " + offset_y)

%% Esercizio 2
% Implementazione manuale della cross-correlazione 2D
% Calcolare manualmente la cross correlazione 2D tra le 
% matrici X1 e X2. Confrontare con il risultato del comando matlab
% xcorr2(X1,X2)
%
% Suggerimento: usare la versione più ottimizzata:
% si fa zero padding della matrice immagine (la matrice più
% grande) e si fa scorrere il kernel
% 

clear all
close all

X1 =[1     2     5     2     5;
     3     4     5     4     2;
     5     2     3     2     2;
     2     3     2     4     2;
     3     4     2     2     3];
     
     
X2 =[3     3     2;
     2     3     4;
     4     5     4];

 
[R1, C1] = size(X1);
[R2, C2] = size(X2);

% zero padding
if R1 > R2
    result = zeros(R1 + R1 - R2, C1 + C1- C2);
    template = zeros(R1 + (R1-R2)*2,C1 + (C1 - C2)*2);
    template(R2:(R1 + R1 - R2), C2:(C1 + C1 - C2)) = X1;
    matrice = X2;
else
    result = zeros(R2 + R2 - R1, C2 + C2 - C1);
    template = zeros(R2 + (R2-R1)*2,C2 + (C2 - C1)*2);
    template(R1:(R2 + R2 - R1), C1:(C2 + C2 - C1)) = X2;
    matrice = X1;
end


[rows, cols] = size(result);

for i=1:rows
    for j=1:cols
        result(i,j) = sum(template(i:i+min([R1 R2]-1), j:j+min([C1 C2]-1)) .* matrice,"all");
    end
end

disp(result)

