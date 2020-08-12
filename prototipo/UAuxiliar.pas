unit UAuxiliar;

interface
uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  Menus, ExtCtrls, StdCtrls, Buttons;

   function  calcula_paralaxe(distancia:real):real;
   procedure inicializa(Imagem_Original,Imagem1,Imagem2:TImage);
   procedure gera_anaglifo(ImagemAnaglifo,Imagem1,Imagem2:TImage;Paralaxe:Integer);
   procedure inicializa_luz(Imagem:TImage;Paralaxe:Integer);
   function  acerta_paralaxe(Paralaxe:real):integer;

implementation
uses UStatus;

function calcula_paralaxe(distancia:real):real;
begin
   result :=  Distancia / 30 ;

end;

// Esta função serve para acertar a paralaxe que será aplicada na imagem
// a paralaxe eh calculada em cm pela função "calcula_paralaxe"
//então este valor precisa ser convertido para pixels
function acerta_paralaxe(Paralaxe:real):integer;
begin
   result := round(Paralaxe * 25);
end;

procedure inicializa(Imagem_Original,Imagem1,Imagem2:TImage);
var
   Cor : tcolor;
   x,
   y :integer;
   MaxCor,
   MinCor,
   CorPonto : longint;
   ValorRGB,
   CorFinalAzul,
   CorFinalVermelho:byte;
   aux1,aux2:byte;
   QtdPontos,
   TotalPontos : longint;

begin
   MaxCor    := 0;
   MinCor    := 99999999;

   FrmStatus.Show;
   FrmStatus.Caption := 'Progresso (Passo 1 de 2)';
   QtdPontos := 0;
   TotalPontos := Imagem1.Height * Imagem1.Width;

   For y := 1 to Imagem1.height do
     for x := 1 to Imagem1.Width do
     begin
        Inc(QtdPontos);
        Cor := ColortoRGB(Imagem_Original.Canvas.Pixels[x,y]);
        CorPonto := ColorToRGB(Cor);
        if CorPonto >= MaxCor then
           MaxCor := CorPonto;
        if CorPonto <= MinCor then
           MinCor := CorPonto;

        FrmStatus.ProgressBar.Progress := (QtdPontos * 100) div TotalPontos;

     end;

   //Fazer com que a Max e Min cor fiquem entre 0 e 255
   MaxCor := (MaxCor * 255) div 16777215;
   MinCor := (MinCor * 255) div 16777215;

   FrmStatus.Caption := 'Progresso (Passo 2 de 2)';
   QtdPontos := 0;
   TotalPontos := Imagem1.Height * Imagem1.Width;

   For y := 1 to Imagem1.height do
     for x := 1 to Imagem1.Width do
     begin
        Inc(QtdPontos);
        //Filtra Vermelho
        Cor := ColortoRGB(Imagem_Original.Canvas.Pixels[x,y]);
        ValorRGB := GetRValue(Cor); //Cor do ponto
        aux1:=MaxCor-MinCor;
        aux2:=MaxCor-ValorRGB;
        CorFinalVermelho:= ( (aux1*255)-(Aux2*255) ) div Aux1;
        Imagem1.Canvas.Pixels[x,y]:=RGB(CorFinalVermelho,0,0);
        //Filtra Azul
        Cor := ColortoRGB(Imagem_Original.Canvas.Pixels[x,y]);
        ValorRGB := GetBValue(Cor); //Cor do ponto
        aux1:=MaxCor-MinCor;
        aux2:=MaxCor-ValorRGB;
        CorFinalAzul := ( (aux1*255)-(Aux2*255) ) div Aux1;
        Imagem2.Canvas.Pixels[x,y]:=RGB(0,0,CorFinalAzul);

        FrmStatus.ProgressBar.Progress := (QtdPontos * 100) div TotalPontos;

     end;

   FrmStatus.Close;

end;

procedure gera_anaglifo(ImagemAnaglifo,Imagem1,Imagem2:TImage;Paralaxe:Integer);
var
   y,
   x : integer;
   QtdPontos,
   TotalPontos : longint;
begin

   FrmStatus.Show;
   FrmStatus.Caption := 'Progresso (Passo 1 de 1)';

   QtdPontos := 0;
   TotalPontos := Imagem1.Height * Imagem1.Width;

   ImagemAnaglifo.Width := ImagemAnaglifo.Width - Paralaxe;

   if Paralaxe > 0 then
   begin
      For y := 1 to ImagemAnaglifo.Height do
         For x := 1 to ImagemAnaglifo.Width do
         begin
            Inc(QtdPontos);
            ImagemAnaglifo.Canvas.Pixels[x,y] := Imagem2.Canvas.Pixels[x,y] +
                                                 Imagem1.Canvas.Pixels[x+Paralaxe,y];

          FrmStatus.ProgressBar.Progress := (QtdPontos * 100) div TotalPontos;

        end;
   end
   else
   if Paralaxe < 0 then
   begin
      For y := 1 to ImagemAnaglifo.Height do
         For x := 1 to ImagemAnaglifo.Width do
         begin
            Inc(QtdPontos);
            ImagemAnaglifo.Canvas.Pixels[x,y] := Imagem1.Canvas.Pixels[x,y] +
                                                 Imagem2.Canvas.Pixels[x-Paralaxe,y];

         FrmStatus.ProgressBar.Progress := (QtdPontos * 100) div TotalPontos;

         end;
   end
   else
   if Paralaxe = 0 then
   begin

      For y := 1 to ImagemAnaglifo.Height do
         For x := 1 to ImagemAnaglifo.Width do
         begin
            Inc(QtdPontos);
            ImagemAnaglifo.Canvas.Pixels[x,y] := Imagem1.Canvas.Pixels[x,y] +
                                                 Imagem2.Canvas.Pixels[x,y];

         FrmStatus.ProgressBar.Progress := (QtdPontos * 100) div TotalPontos;

         end;
   end;

   FrmStatus.Close;

end;

procedure inicializa_luz(Imagem:TImage;Paralaxe:integer);
var
  x,y : integer;
  QtdPontos,
  TotalPontos : longint;
begin

   QtdPontos := 0;
   TotalPontos := (Imagem.Height) * (Imagem.Width - Paralaxe);

   FrmStatus.Show;
   FrmStatus.Caption := 'Progresso (Passo 1 de 1)';

   for y:=1 to Imagem.Height do
      for x := 1 to Imagem.Width - Paralaxe do
      begin
         Inc(QtdPontos);
         Imagem.Canvas.Pixels[x,y] := Imagem.Canvas.Pixels[x+Paralaxe,y];
         FrmStatus.ProgressBar.Progress := (QtdPontos * 100) div TotalPontos;
      end;

   FrmStatus.Close;
end;


end.
