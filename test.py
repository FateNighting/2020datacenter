import sys
from test2 import func
if __name__ == '__main__':
    a = []
    for i in range(1, len(sys.argv)):
        a.append((float(sys.argv[i])))

    print(func(a[0],a[1]))
    print("章鹏大帅哥1！")
    print("章鹏大帅哥2！")