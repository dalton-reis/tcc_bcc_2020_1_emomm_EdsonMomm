unit UStatus;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  Gauges;

type
  TFrmStatus = class(TForm)
    ProgressBar: TGauge;
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  FrmStatus: TFrmStatus;

implementation

{$R *.DFM}

end.
