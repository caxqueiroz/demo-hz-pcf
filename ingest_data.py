import requests


def generate_payload():
    payload = '1461790265377841,1.10849,1.10779,AUD_NZD'
    return payload


def send_payload(payload):
    r = requests.post("http://hz-pcf-custom-pojo.apps.hack2hire.net", data=payload)
    return r


def get_total():
    r = requests.get('http://hz-pcf-custom-pojo.apps.hack2hire.net/total')
    return r.text


def get_instrument(instrument):
    r = requests.get("http://hz-pcf-custom-pojo.apps.hack2hire.net/instrument/%s" % instrument)
    return r.text

if __name__ == '__main__':
    r = send_payload(generate_payload())
    print('total %s' % get_total())
    print('data %s ' % get_instrument('AUD_NZD'))

