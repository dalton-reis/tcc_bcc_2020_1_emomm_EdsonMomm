unit USobre;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls, ExtCtrls, jpeg;

type
  TFrmSobre = class(TForm)
    Image1: TImage;
    StaticText1: TStaticText;
    Label1: TLabel;
    Label2: TLabel;
    Label3: TLabel;
    Label4: TLabel;
    Label5: TLabel;
    Label6: TLabel;
    Label7: TLabel;
    procedure FormClick(Sender: TObject);
    procedure Image1Click(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  FrmSobre: TFrmSobre;

implementation

{$R *.DFM}

procedure TFrmSobre.FormClick(Sender: TObject);
begin
   FrmSobre.Close;
end;

procedure TFrmSobre.Image1Click(Sender: TObject);
begin
   FrmSobre.Close;
end;

end.
