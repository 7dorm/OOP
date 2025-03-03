from functools import lru_cache

# Morse code dictionary
MORSE_CODE_DICT = {
    'A': '.-', 'B': '-...', 'C': '-.-.', 'D': '-..', 'E': '.', 'F': '..-.',
    'G': '--.', 'H': '....', 'I': '..', 'J': '.---', 'K': '-.-', 'L': '.-..',
    'M': '--', 'N': '-.', 'O': '---', 'P': '.--.', 'Q': '--.-', 'R': '.-.',
    'S': '...', 'T': '-', 'U': '..-', 'V': '...-', 'W': '.--', 'X': '-..-',
    'Y': '-.--', 'Z': '--..',

    '0': '-----', '1': '.----', '2': '..---', '3': '...--', '4': '....-',
    '5': '.....', '6': '-....', '7': '--...', '8': '---..', '9': '----.',

    '&': '.-...', "'": '.----.', '@': '.--.-.', ')': '-.--.-', '(': '-.--.',
    ':': '---...', ',': '--..--', '=': '-...-', '!': '-.-.--', '.': '.-.-.-',
    '-': '-....-', '+': '.-.-.', '"': '.-..-.', '?': '..--..', '/': '-..-.'
}

# Reverse mapping for decoding
REVERSE_MORSE_CODE_DICT = {value: key for key, value in MORSE_CODE_DICT.items()}

def generate_splits(s, max_splits):
    


def decode_morse_variants(morse_code):
    variants = []
    # Determine the maximum number of splits needed
    # Assuming each Morse character is at least 1 character long
    # and there are no spaces in the input
    max_splits = len(morse_code)  # This is a very high upper bound

    # Generate all possible splits
    all_splits = generate_splits(morse_code, max_splits)

    print(*all_splits)
    for split in all_splits:
        decoded_chars = []
        valid = True
        for char in split:
            if char in REVERSE_MORSE_CODE_DICT:
                decoded_chars.append(REVERSE_MORSE_CODE_DICT[char])
            else:
                valid = False
                break
        if valid:
            variants.append(''.join(decoded_chars))
    return variants

# Example usage
if __name__ == "__main__":
    morse_input = "--.-------...-.-----..-.--...."
    
    decoded_variants = decode_morse_variants(morse_input)
    
    print(f"Possible Decodings ({len(decoded_variants)}):")
    for variant in decoded_variants:
        print(variant)
