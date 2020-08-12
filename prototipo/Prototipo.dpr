program Prototipo;

uses
  Forms,
  UPrototipo in 'UPrototipo.pas' {FrmPrototipo},
  ULuz in 'ULuz.pas' {FrmLuz},
  UAnaglifo in 'UAnaglifo.pas' {FrmAnaglifo},
  UAuxiliar in 'UAuxiliar.pas',
  UStatus in 'UStatus.pas' {FrmStatus},
  USobre in 'USobre.pas' {FrmSobre};

{$R *.RES}

begin
  Application.Initialize;
  Application.CreateForm(TFrmPrototipo, FrmPrototipo);
  Application.CreateForm(TFrmAnaglifo, FrmAnaglifo);
  Application.CreateForm(TFrmStatus, FrmStatus);
  Application.CreateForm(TFrmSobre, FrmSobre);
  Application.Run;
end.
