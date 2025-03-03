from functools import lru_cache

MORSE_CODE_DICT = {
    'A': '.-', 'B': '-...', 'C': '-.-.', 'D': '-..', 'E': '.', 'F': '..-.',
    'G': '--.', 'H': '....', 'I': '..', 'J': '.---', 'K': '-.-', 'L': '.-..',
    'M': '--', 'N': '-.', 'O': '---', 'P': '.--.', 'Q': '--.-', 'R': '.-.',
    'S': '...', 'T': '-', 'U': '..-', 'V': '...-', 'W': '.--', 'X': '-..-',
    'Y': '-.--', 'Z': '--..'
}

# Reverse mapping for decoding
REVERSE_MORSE_CODE_DICT = {value: key for key, value in MORSE_CODE_DICT.items()}

def decode_morse(morse_code):
    # Split the morse code into words based on '/'
    words = morse_code.strip().split(' / ')
    
    decoded_words = []
    for word in words:
        # Split the word into individual morse characters
        chars = word.split(' ')
        decoded_chars = []
        for char in chars:
            if char == '':
                continue  # Skip empty characters
            decoded_char = REVERSE_MORSE_CODE_DICT.get(char, '')
            if decoded_char:
                decoded_chars.append(decoded_char)
            else:
                decoded_chars.append('?')  # Unknown character
        decoded_word = ''.join(decoded_chars)
        decoded_words.append(decoded_word)
    
    return ' '.join(decoded_words)

def generate_space_variants(s):
    n = len(s)
    variants = []

    @lru_cache(maxsize=None)
    def backtrack(index, current):
        if index == n:
            variants.append(current)
            return
        # Не вставляем пробел
        backtrack(index + 1, current + s[index])
        # Вставляем пробел
        backtrack(index + 1, current + ' ' + s[index])

    backtrack(1, s[0])
    return variants

# Пример использования
if __name__ == "__main__":
    f = open("result1.txt", 'w')
    input_string = "-------...-.-----..-.--"[::-1] # H ... G
    variants = generate_space_variants(input_string)
    print(f"Все варианты расстановки пробелов для '{input_string}':")
    for variant in variants:
        c = decode_morse(variant)
        if "?" not in c:
            f.write("H" + c + "G" + '\n')
