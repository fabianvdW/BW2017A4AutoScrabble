#!/usr/bin/env python2
# coding=utf8


# Einlesen der zu Kuerzelliste
def get_kuerzel_liste():
    output = []
    # Öffnen der Kuerzelliste im Lese-Modus
    with open('kuerzelliste.txt', 'r') as f:
        for line in f:
            # '\n' nicht mit in die Liste speichern
            output += [line.rstrip('\n')]
    # Zurueckgeben der Liste
    return output


# Extrahieren der naechsten moeglichen Anfangs-Buchstaben des naechsten Kennzeichens aus einer Zeichenkette
# kz : Ein 'Endteil' des eingegebenen Begriffs
# Beispiel : 'BIBER' -> 'BIBER' oder 'IBER', ...
def get_gueltige_rest_autozeichen(kennzeichen, kuerzel_liste):
    """

    :param kennzeichen: Zeichenkette
    :param kuerzel_liste: Liste, die alle moeglichen Anfaenge enthaellt
    :return: Die naechsten moeglichen Kennzeichen-Anfaenge
    """
    output = []
    if len(kennzeichen) >= 3:
        parts = [kennzeichen[:1], kennzeichen[:2], kennzeichen[:3]]
    elif len(kennzeichen) == 2:
        parts = [kennzeichen[:1], kennzeichen[:2]]
    elif len(kennzeichen) == 1:
        parts = [kennzeichen[:1]]
    else:
        parts = []
    for part in parts:
        # Ueberpruefen, ob es dieses Kuerzel ueberhaupt gibt (ob es also in der kuerzel_liste vorhanden ist)
        if part in kuerzel_liste:
            output += [part]
    # Zurueckgeben der moeglichen naechsten Kennzeichen-Anfaenge
    return output


def get_moeglichkeiten(rest_kz, kz_dict, jetziges_kz):
    """

    :param rest_kz: Rest der Eingabe
    :param kz_dict: Woerterbuch, key = Rest eines Kennzeichen, value = moergliche naechste Kennzeichen-Anfaenge
    :param jetziges_kz: Bisherige Kennzeichen
    :return:
    """
    # Alle naechsten moeglichen Anfaenge eines Kennzeichens
    moeglickheiten = kz_dict[rest_kz]
    output = []
    for kz in moeglickheiten:
        # puffer speichert den 2. Teil eines Kennzeichens (z.B. ['-A', '-AB')
        puffer = []
        # kzs speichert moegliche Kennzeichen (z.B. [ AB-C, A-BC, ..])
        kzs = []
        for i in range(2):
            if len(rest_kz[len(kz):]) > 0:
                # Hinzufuegen des moeglichen Kennzeichens zu den entsprechenden Listen
                puffer += ['-' + rest_kz[len(kz):][:i + 1]]
                kzs += [jetziges_kz + [str(kz + puffer[i])]]
        if len(puffer) > 0:
            output += [[kzs[0], rest_kz[len(kz) + len(puffer[0]) - 1:]]]
        if len(puffer) > 1:
            output += [[kzs[1], rest_kz[len(kz) + len(puffer[1]) - 1:]]]
    return output


def generate_kz_dict(kz, kz_liste):
    kz_dict = {}
    for i in range(len(kz)):
        kz_dict[kz[i:]] = get_gueltige_rest_autozeichen(kz[i:], kz_liste)
    kz_dict[''] = ''
    return kz_dict


def check_for_good_kzs(kzs):
    output = []
    for kz in kzs:
        if kz[1] == "":
            output += [kz[0]]
    return output


def remove_doublings(kzs):
    output = []
    for kz in kzs:
        if kz not in output:
            output += [kz]
    return output


def key_nicht_in_anderer_liste(liste, own_key, key):
    for tkey in liste:
        if tkey != own_key:
            print tkey + '  -   ' + key
            if tkey.startswith(key):
                return False
    return True


def get_entire_len(dict):
    output = 0
    for key in dict:
        output += len(dict[key])
    return output

kuerzel_liste = get_kuerzel_liste()
kz = str(raw_input('Geben Sie ein Wort ein: '))
kz_dict = generate_kz_dict(kz, kuerzel_liste)
kzs = get_moeglichkeiten(kz, kz_dict, [''])
results = []
i = 0
while not kzs == []:
    new_kz = []
    for kz in kzs:
        new_kz += get_moeglichkeiten(kz[1], kz_dict, kz[0])
    kzs = new_kz
    check_result = check_for_good_kzs(kzs)
    for kz in check_result:
        kz.pop(0)
        results += [kz]
    i += 1

results = remove_doublings(results)
print "Es gibt " + str(len(results)) + " moegliche Kennzeichen: "
for kz in results:
    print kz
