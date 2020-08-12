unit UAnaglifo;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  ExtCtrls, StdCtrls, ComCtrls, Gauges, UAuxiliar, Buttons, ExtDlgs, Jpeg;

type
  TFrmAnaglifo = class(TForm)
    Panel1: TPanel;
    Panel2: TPanel;
    BtInicializa: TButton;
    BtGera: TButton;
    BtInv: TButton;
    Imagem1: TImage;
    Imagem2: TImage;
    ImagemAnaglifo: TImage;
    EdDistancia: TEdit;
    Label1: TLabel;
    Label2: TLabel;
    Label3: TLabel;
    EdParalaxe: TEdit;
    BitBtn1: TBitBtn;
    BtSalvar: TButton;
    SaveDialog: TSavePictureDialog;
    procedure BtInicializaClick(Sender: TObject);
    procedure BtInvClick(Sender: TObject);
    procedure BtGeraClick(Sender: TObject);
    procedure EdDistanciaChange(Sender: TObject);
    procedure BtSalvarClick(Sender: TObject);


  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  FrmAnaglifo: TFrmAnaglifo;

implementation

uses UPrototipo;

{$R *.DFM}

procedure TFrmAnaglifo.BtInicializaClick(Sender: TObject);
begin
   Inicializa(FrmPrototipo.Imagem_Original,Imagem1,Imagem2);
   BtGera.Enabled := True;
   BtInv.Enabled  := True;
   BtSalvar.Enabled := True;
end;

procedure TFrmAnaglifo.BtInvClick(Sender: TObject);
begin
   If Imagem1.Visible then
   begin
      Imagem1.Visible := False;
      Imagem2.Visible := True;
      ImagemAnaglifo.Visible := False;
   end
   else
   If Imagem2.Visible then
   begin
      Imagem2.Visible := False;
      Imagem1.Visible := False;
      ImagemAnaglifo.Visible := True;
   end
   else
   If ImagemAnaglifo.Visible then
   begin
      Imagem1.Visible := True;
      Imagem2.Visible := False;
      ImagemAnaglifo.Visible := False;
   end;
end;

procedure TFrmAnaglifo.BtGeraClick(Sender: TObject);
var
   Paralaxe : integer;
begin
   Paralaxe := Acerta_Paralaxe(calcula_paralaxe(strtoint(EdDistancia.text)));
   ImagemAnaglifo.Width  := Imagem2.Width;
//   ImagemAnaglifo.Height := Imagem2.Height;
   Gera_anaglifo(ImagemAnaglifo,Imagem1,Imagem2,Paralaxe);
end;

procedure TFrmAnaglifo.EdDistanciaChange(Sender: TObject);

begin
   If EdDistancia.Text = '' then
      EdDistancia.Text := '0';
                                    {Transformar em cm}
      EdParalaxe.Text := FloattoStr(calcula_paralaxe(StrtoInt(EdDistancia.Text)));

end;

procedure TFrmAnaglifo.BtSalvarClick(Sender: TObject);
var
   ImagemJpeg:TJPegImage;
begin

   If SaveDialog.Execute then
      If SaveDialog.FilterIndex = 2 then
      begin
         ImagemJpeg := TJpegImage.Create;
         try
            with ImagemJpeg do
            begin
               Assign(ImagemAnaglifo.Picture.Bitmap);
               SaveToFile(SaveDialog.FileName);
            end;
         finally
            ImagemJpeg.Free;
         end;
      end
      else
         ImagemAnaglifo.Picture.SaveToFile(SaveDialog.FileName);
end;

end.
