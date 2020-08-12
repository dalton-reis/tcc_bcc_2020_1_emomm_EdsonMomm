object FrmLuz: TFrmLuz
  Left = 164
  Top = 103
  Width = 696
  Height = 534
  Align = alClient
  Caption = 'Luz Intermitente'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  PixelsPerInch = 96
  TextHeight = 13
  object Panel: TPanel
    Left = 129
    Top = 0
    Width = 559
    Height = 507
    Align = alClient
    BevelOuter = bvNone
    TabOrder = 0
    object Imagem1: TImage
      Left = 0
      Top = 0
      Width = 552
      Height = 507
      Visible = False
    end
    object Imagem2: TImage
      Left = 0
      Top = 0
      Width = 552
      Height = 507
      Visible = False
    end
  end
  object Panel1: TPanel
    Left = 0
    Top = 0
    Width = 129
    Height = 507
    Align = alLeft
    BevelOuter = bvNone
    TabOrder = 4
    object Label1: TLabel
      Left = 4
      Top = 278
      Width = 117
      Height = 13
      Caption = 'Distância do Observador'
    end
    object Label2: TLabel
      Left = 30
      Top = 294
      Width = 85
      Height = 13
      Caption = 'até o Monitor (cm)'
    end
    object Label3: TLabel
      Left = 17
      Top = 345
      Width = 53
      Height = 13
      Caption = 'Paralaxe = '
    end
    object BtAplicar: TButton
      Left = 24
      Top = 168
      Width = 89
      Height = 25
      Caption = 'Aplica Paralaxe'
      TabOrder = 0
      OnClick = BtAplicarClick
    end
    object EdDistancia: TEdit
      Left = 56
      Top = 312
      Width = 33
      Height = 21
      Hint = '1/30 desta distância representa a paralaxe'
      MaxLength = 4
      ParentShowHint = False
      ShowHint = True
      TabOrder = 1
      Text = '30'
      OnChange = EdDistanciaChange
    end
    object EdParalaxe: TEdit
      Left = 76
      Top = 341
      Width = 45
      Height = 21
      TabOrder = 2
      Text = '1'
    end
    object BitBtn1: TBitBtn
      Left = 24
      Top = 400
      Width = 75
      Height = 25
      Caption = 'Fechar'
      TabOrder = 3
      Kind = bkClose
      Layout = blGlyphRight
    end
  end
  object BtRG: TButton
    Left = 24
    Top = 231
    Width = 89
    Height = 25
    Caption = 'Azul/Vermelho'
    TabOrder = 3
    OnClick = BtRGClick
  end
  object BtInicia: TBitBtn
    Left = 24
    Top = 199
    Width = 89
    Height = 25
    Caption = 'Inicia'
    TabOrder = 2
    OnClick = BtIniciaClick
  end
  object BtInicializar: TBitBtn
    Left = 24
    Top = 136
    Width = 89
    Height = 25
    Caption = 'Inicializar Figura'
    TabOrder = 1
    OnClick = BtInicializarClick
  end
  object Timer: TTimer
    Enabled = False
    Interval = 17
    OnTimer = TimerTimer
    Left = 24
    Top = 88
  end
end
