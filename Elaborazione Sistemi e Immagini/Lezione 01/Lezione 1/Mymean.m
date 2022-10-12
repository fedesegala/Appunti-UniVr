function [output] = Mymean(input)
% Restituisce il valore medio di una matrice o di un vettore passato in
% input. Nel caso di vettori restituisce il singolo valor medio, altrimenti
% un vettore riga contenente il valore medio di ogni colonna

dimensione = size(input);
output = ones(1,dimensione(1));

for i = 1:dimensione(1)
    somma = 0;
    for j = 1:dimensione(2)
        somma = somma + input(i,j);
    end
    disp(numel(input(dimensione(i))));
    output(i) = somma/dimensione(2);
end

end


