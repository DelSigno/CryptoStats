format long
aes = importdata('data_AlgAES');
serpent = importdata('data_AlgSERPENT');
simon = importdata('data_AlgSIMON');
speck = importdata('data_AlgSPECK');

graphheight = .25;
%Figure 1
figure(1)
plot(aes)
axis([0 length(aes) 0 graphheight]);



%Figure 2
figure(2)
plot(serpent)
axis([0 length(serpent) 0 graphheight]);

%Figure 3
figure(3)
plot(simon)
axis([0 length(simon) 0 graphheight]);

%Figure 4
figure(4)
plot(speck)
axis([0 length(speck) 0 graphheight]);