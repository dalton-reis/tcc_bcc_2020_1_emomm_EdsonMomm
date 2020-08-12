unit ULuz;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  Gauges, ExtCtrls, StdCtrls, Buttons, UAuxiliar;

type
  TFrmLuz = class(TForm)
    Panel: TPanel;
    Panel1: TPanel;
    Imagem1: TImage;
    Imagem2: TImage;
    BtInicializar: TBitBtn;
    BtInicia: TBitBtn;
    Timer: TTimer;
    BtRG: TButton;
    BtAplicar: TButton;
    Label1: TLabel;
    Label2: TLabel;
    EdDistancia: TEdit;
    Label3: TLabel;
    EdParalaxe: TEdit;
    BitBtn1: TBitBtn;
    procedure BtInicializarClick(Sender: TObject);
    procedure BtRGClick(Sender: TObject);
    procedure TimerTimer(Sender: TObject);
    procedure BtIniciaClick(Sender: TObject);
    procedure Alterna_Imagens;
    procedure BtAplicarClick(Sender: TObject);
    procedure EdDistanciaChange(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  FrmLuz: TFrmLuz;
  Img1Front : boolean; // indica se a imagem em vermelho esta sendo exibida
  Imagem_Inicializada :boolean;

implementation

uses UPrototipo;

{$R *.DFM}

procedure TFrmLuz.BtInicializarClick(Sender: TObject);
begin

   Imagem1.Width := FrmPrototipo.Imagem_Original.Width;
   Imagem2.Width := FrmPrototipo.Imagem_Original.Width;

   Inicializa(FrmPrototipo.Imagem_Original,Imagem1,Imagem2);

   Imagem2.Visible:=true;
   Imagem1.Visible:=true;
   Imagem1.BringToFront;
   Img1Front:=true;

   Imagem_Inicializada := true;

end;

procedure TFrmLuz.Alterna_Imagens;
// Tem o objetivo de alternar as imagens em verde e vermelho na tela
begin
   If Img1Front then
   begin
      Imagem1.SendToBack;
      Img1Front := false;
   end
   else
   begin
      Imagem1.BringToFront;
      Img1Front:=true;
   end;

{   If Imagem1.Visible then
   begin
      Imagem2.Visible := true;
      Imagem1.Visible := false;
   end
   else
   begin
      Imagem1.Visible := true;
      Imagem2.Visible := false;
   end;
}
end;

procedure TFrmLuz.BtRGClick(Sender: TObject);
begin
   Alterna_Imagens;
end;

procedure TFrmLuz.TimerTimer(Sender: TObject);
begin
   Alterna_Imagens;
end;

procedure TFrmLuz.BtIniciaClick(Sender: TObject);
begin
   If Timer.Enabled then
      Timer.Enabled := false
   else
      Timer.Enabled := true;

end;

procedure TFrmLuz.BtAplicarClick(Sender: TObject);
var
  Paralaxe : integer;

begin

   Paralaxe := Round(Calcula_Paralaxe(StrtoFloat(EdDistancia.Text)));

   //Se a imagem não foi inicializada eh necessário inicializá-la;
   if Imagem_Inicializada = false then
      BtInicializar.Click; //Dispara a Inicialização da figura

   If Paralaxe > 0 then
   begin
      Paralaxe := Acerta_paralaxe(Calcula_Paralaxe(StrtoFloat(EdDistancia.Text)));
      Inicializa_Luz(Imagem1,Paralaxe);
   end
   else
   if Paralaxe < 0 then
   begin
      Paralaxe := -Acerta_paralaxe(Calcula_Paralaxe(StrtoFloat(EdDistancia.Text)));
      Inicializa_Luz(Imagem2,-Paralaxe);
   end
   else
      Inicializa_Luz(Imagem2,0);

   Imagem1.Width := Imagem1.Width - Paralaxe;
   Imagem2.Width := Imagem2.Width - Paralaxe;

end;

procedure TFrmLuz.EdDistanciaChange(Sender: TObject);
begin
   If EdDistancia.Text = '' then
      EdDistancia.Text := '0';
                                    {Transformar em cm}
      EdParalaxe.Text := FloattoStr(calcula_paralaxe(StrtoInt(EdDistancia.Text)));

   Imagem_Inicializada := false; //Tenho que inicializar a figura novamente

end;

begin
  Imagem_Inicializada := false;

end.
