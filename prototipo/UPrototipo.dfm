object FrmPrototipo: TFrmPrototipo
  Left = 164
  Top = 103
  Width = 696
  Height = 533
  Align = alClient
  Caption = 'Prototipo'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  Menu = Menu
  OldCreateOrder = False
  PixelsPerInch = 96
  TextHeight = 13
  object Panel2: TPanel
    Left = 0
    Top = 0
    Width = 145
    Height = 487
    Align = alLeft
    BevelOuter = bvNone
    TabOrder = 3
  end
  object Panel: TPanel
    Left = 145
    Top = 0
    Width = 543
    Height = 487
    Align = alClient
    BevelOuter = bvNone
    TabOrder = 0
    object Imagem_Original: TImage
      Left = 0
      Top = 0
      Width = 545
      Height = 489
      AutoSize = True
    end
  end
  object BtLuz: TBitBtn
    Left = 24
    Top = 120
    Width = 97
    Height = 25
    Caption = 'Luz Intermitente'
    TabOrder = 1
    OnClick = BtLuzClick
  end
  object BtAnaglifo: TBitBtn
    Left = 24
    Top = 152
    Width = 97
    Height = 25
    Caption = 'Anaglifo'
    TabOrder = 2
    OnClick = BtAnaglifoClick
  end
  object Menu: TMainMenu
    Left = 8
    Top = 32
    object Arquivo1: TMenuItem
      Caption = 'Arquivo'
      object Abrir1: TMenuItem
        Caption = 'Abrir'
        OnClick = Abrir1Click
      end
    end
    object Sair1: TMenuItem
      Caption = 'Sair'
      OnClick = Sair1Click
    end
    object Sobre1: TMenuItem
      Caption = 'Sobre'
      OnClick = Sobre1Click
    end
  end
  object OpenDialog: TOpenPictureDialog
    Filter = 'Arquivo Bitmap (*.bmp)|*.bmp'
    Options = [ofHideReadOnly, ofFileMustExist, ofEnableSizing]
    Left = 40
    Top = 64
  end
end
