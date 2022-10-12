function [output] = checkSym(input)
dimensione = size(input);

if size(dimensione) ~= [1 2]
    output = 0;
elseif dimensione(1) ~= dimensione(2)
    output = 0;
elseif input' == input %#ok<BDSCA> 
    output = 1;
else
    output = 0;

end
