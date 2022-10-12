function [output] = Mymean(input)
% Restituisce il valore medio di una matrice o di un vettore passato in
% input. Nel caso di vettori restituisce il singolo valor medio, altrimenti
% un vettore riga contenente il valore medio di ogni colonna

dimensione = size(input)
if dimensione(1) > 1 & dimensione(2) > 1
    output = zeros(1,dimensione(2))
    
    
    for i = 1:dimensione(1)
        for j=1:dimensione(2)
            output(1,j) = output(1,j) + input(i,j);
        end
    end
    
    output = output/dimensione(1);
elseif dimensione(1) == 1
    output = sum(input) / dimensione(2)
else
    output = sum(input) / dimensione(1)
end

end



