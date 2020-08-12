unit UPrototipo;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  Menus, ExtCtrls, StdCtrls, Buttons, ExtDlgs;

type
  TFrmPrototipo = class(TForm)
    Panel: TPanel;
    Menu: TMainMenu;
    Arquivo1: TMenuItem;
    Sair1: TMenuItem;
    Abrir1: TMenuItem;
    Imagem_Original: TImage;
    BtLuz: TBitBtn;
    BtAnaglifo: TBitBtn;
    Panel2: TPanel;
    OpenDialog: TOpenPictureDialog;
    Sobre1: TMenuItem;
    procedure Abrir1Click(Sender: TObject);
    procedure Sair1Click(Sender: TObject);
    procedure BtLuzClick(Sender: TObject);
    procedure BtAnaglifoClick(Sender: TObject);
    procedure Sobre1Click(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  FrmPrototipo: TFrmPrototipo;
  ArquivoCarregado : boolean = false;

implementation

uses ULuz,UAnaglifo, USobre;

{$R *.DFM}

procedure TFrmPrototipo.Abrir1Click(Sender: TObject);
begin
   If OpenDialog.Execute then
   begin
      Imagem_Original.Picture.LoadFromFile(OpenDialog.Filename);
      ArquivoCarregado := true;
   end;

end;

procedure TFrmPrototipo.Sair1Click(Sender: TObject);
begin
   Application.Terminate;
end;

procedure TFrmPrototipo.BtLuzClick(Sender: TObject);
begin
   If ArquivoCarregado then
   begin
      Application.CreateForm(TFrmLuz, FrmLuz);
      FrmLuz.Imagem1.Height := Imagem_original.Height;
      FrmLuz.Imagem1.Width  := Imagem_Original.Width;
      FrmLuz.Imagem2.Height := Imagem_original.Height;
      FrmLuz.Imagem2.Width  := Imagem_Original.Width;
      FrmLuz.ShowModal;
      FrmLuz.Destroy;
   end
   else
      ShowMessage('É necessário carregar uma imagem');
end;

procedure TFrmPrototipo.BtAnaglifoClick(Sender: TObject);
begin
   If ArquivoCarregado then
   begin
      Application.CreateForm(TFrmAnaglifo, FrmAnaglifo);
      FrmAnaglifo.Imagem1.Height := Imagem_original.Height;
      FrmAnaglifo.Imagem1.Width  := Imagem_Original.Width;
      FrmAnaglifo.Imagem2.Height := Imagem_original.Height;
      FrmAnaglifo.Imagem2.Width  := Imagem_Original.Width;
      FrmAnaglifo.ImagemAnaglifo.Height := Imagem_original.Height;
      FrmAnaglifo.ImagemAnaglifo.Width  := Imagem_Original.Width;
      FrmAnaglifo.ShowModal;
      FrmAnaglifo.Destroy;
   end
   else
      ShowMessage('É necessário carregar uma imagem');

end;

procedure TFrmPrototipo.Sobre1Click(Sender: TObject);
begin
    FrmSobre.ShowModal;
end;

end.
