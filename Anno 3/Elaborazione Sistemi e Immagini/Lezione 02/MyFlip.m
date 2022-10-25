function [output] = MyFlip(mu)
    dim = size(mu);
    mu1 = zeros(1,dim(2));
    indice = 1;

    for i = dim(2):-1:1
        mu1(1,indice) = mu1(1,indice) + mu(1,i);
        indice = indice + 1;
    end

    output = [mu1, mu];

end