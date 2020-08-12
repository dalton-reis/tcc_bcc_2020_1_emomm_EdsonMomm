object FrmAnaglifo: TFrmAnaglifo
  Left = 164
  Top = 104
  Width = 696
  Height = 531
  Align = alClient
  Caption = 'Anaglífo'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  PixelsPerInch = 96
  TextHeight = 13
  object Panel1: TPanel
    Left = 0
    Top = 0
    Width = 153
    Height = 504
    Align = alLeft
    BevelOuter = bvNone
    TabOrder = 0
    object Label1: TLabel
      Left = 22
      Top = 240
      Width = 117
      Height = 13
      Caption = 'Distância do Observador'
    end
    object Label2: TLabel
      Left = 38
      Top = 256
      Width = 85
      Height = 13
      Caption = 'até o Monitor (cm)'
    end
    object Label3: TLabel
      Left = 25
      Top = 307
      Width = 53
      Height = 13
      Caption = 'Paralaxe = '
    end
    object BtInicializa: TButton
      Left = 43
      Top = 106
      Width = 75
      Height = 25
      Caption = 'Inicializa'
      TabOrder = 0
      OnClick = BtInicializaClick
    end
    object BtGera: TButton
      Left = 43
      Top = 138
      Width = 75
      Height = 25
      Caption = 'Gera Anaglífo'
      Enabled = False
      TabOrder = 1
      OnClick = BtGeraClick
    end
    object BtInv: TButton
      Left = 36
      Top = 170
      Width = 89
      Height = 25
      Caption = 'Vermelho/Azul'
      Enabled = False
      TabOrder = 2
      OnClick = BtInvClick
    end
    object EdDistancia: TEdit
      Left = 64
      Top = 274
      Width = 33
      Height = 21
      Hint = '1/30 desta distância representa a paralaxe'
      MaxLength = 4
      ParentShowHint = False
      ShowHint = True
      TabOrder = 3
      Text = '30'
      OnChange = EdDistanciaChange
    end
    object EdParalaxe: TEdit
      Left = 84
      Top = 303
      Width = 45
      Height = 21
      TabOrder = 4
      Text = '1'
    end
    object BitBtn1: TBitBtn
      Left = 40
      Top = 400
      Width = 75
      Height = 25
      Caption = 'Fechar'
      TabOrder = 5
      Kind = bkClose
      Layout = blGlyphRight
    end
    object BtSalvar: TButton
      Left = 40
      Top = 368
      Width = 75
      Height = 25
      Caption = 'Salvar'
      Enabled = False
      TabOrder = 6
      OnClick = BtSalvarClick
    end
  end
  object Panel2: TPanel
    Left = 153
    Top = 0
    Width = 535
    Height = 504
    Align = alClient
    BevelOuter = bvNone
    TabOrder = 1
    object Imagem1: TImage
      Left = 0
      Top = 0
      Width = 535
      Height = 504
      Visible = False
    end
    object Imagem2: TImage
      Left = 0
      Top = 0
      Width = 535
      Height = 504
      Cursor = crHandPoint
      Visible = False
    end
    object ImagemAnaglifo: TImage
      Left = 0
      Top = 0
      Width = 535
      Height = 504
    end
  end
  object SaveDialog: TSavePictureDialog
    Filter = 'Bitmaps (*.bmp)|*.bmp|JPEG Image File (*.jpg)|*.jpg'
    Options = [ofOverwritePrompt, ofHideReadOnly, ofEnableSizing]
    Left = 24
    Top = 40
  end
end
