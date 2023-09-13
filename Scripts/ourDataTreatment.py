import json
import csv


file = open("backup.json", "r")

jsonData = json.loads(file.read())

collections = jsonData['__collections__']

testes = collections['testes2']


with open('ourDS.csv', 'w', newline='') as file:
    writer = csv.writer(file)
    

    writer.writerow(["testId", "sampleNo", 'acc_x','acc_y','acc_z','gyro_x','gyro_y','gyro_z','azimuth','pitch','roll',"label"])

    for teste in testes.values():
        gyroslen = len(teste['gyroscopicData'])
        accelen = len(teste['accelerometerData'])
        orientationlen = len(teste['orientationData'])

        length = 0
        if gyroslen < accelen and gyroslen < orientationlen:
           length = gyroslen
        
        elif accelen < gyroslen and accelen < orientationlen:
            length = accelen
        else:
            length = orientationlen
            
        for i in range(0,length):
            writer.writerow([teste['id'], i, teste['accelerometerData'][i]['valueX'],teste['accelerometerData'][i]['valueY'],teste['accelerometerData'][i]['valueZ'],teste['gyroscopicData'][i]['valueX'],teste['gyroscopicData'][i]['valueY'],teste['gyroscopicData'][i]['valueZ'],teste['orientationData'][i]['valueX'],teste['orientationData'][i]['valueY'],teste['orientationData'][i]['valueZ'],teste['label']])
            

       


        
        
        
        



